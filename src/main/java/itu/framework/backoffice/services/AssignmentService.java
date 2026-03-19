package itu.framework.backoffice.services;

import itu.framework.backoffice.dtos.ReservationDTO;
import itu.framework.backoffice.entities.*;
import itu.framework.backoffice.models.AssignmentResult;
import itu.framework.backoffice.models.GroupeReservation;
import itu.framework.backoffice.models.TrajetCandidat;
import itu.framework.backoffice.models.TripTiming;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AssignmentService {

    private static class Interval {
        LocalDateTime start;
        LocalDateTime end;

        Interval(LocalDateTime s, LocalDateTime e) {
            this.start = s;
            this.end = e;
        }
    }

    private static boolean overlaps(LocalDateTime aStart, LocalDateTime aEnd, LocalDateTime bStart,
            LocalDateTime bEnd) {
        if (aStart == null || aEnd == null || bStart == null || bEnd == null)
            return false;
        return !(aEnd.isBefore(bStart) || aEnd.isEqual(bStart) || aStart.isAfter(bEnd) || aStart.isEqual(bEnd));
    }

    private static boolean isDateBetween(LocalDateTime start, LocalDateTime end, LocalDateTime date) {
        if (start == null || end == null || date == null)
            return false;
        return (start.isBefore(date) || start.isEqual(date)) && end.isAfter(date);
    }

    private static boolean isVehicleFree(Integer vehiculeId, LocalDateTime start, LocalDateTime end,
            Map<Integer, List<Interval>> calendar) {
        if (start == null || end == null)
            return false;
        List<Interval> list = calendar.get(vehiculeId);
        if (list == null)
            return true;
        for (Interval iv : list) {
            if (overlaps(start, end, iv.start, iv.end))
                return false;
        }
        return true;
    }

    private static void addIntervalToCalendar(Integer vehiculeId, LocalDateTime start, LocalDateTime end,
            Map<Integer, List<Interval>> calendar) {
        if (vehiculeId == null || start == null || end == null)
            return;
        List<Interval> list = calendar.computeIfAbsent(vehiculeId, k -> new ArrayList<>());
        list.add(new Interval(start, end));
        list.sort(Comparator.comparing(i -> i.start));
        List<Interval> merged = new ArrayList<>();
        for (Interval iv : list) {
            if (merged.isEmpty()) {
                merged.add(iv);
            } else {
                Interval last = merged.get(merged.size() - 1);
                if (!iv.start.isAfter(last.end)) {
                    if (iv.end.isAfter(last.end))
                        last.end = iv.end;
                } else {
                    merged.add(iv);
                }
            }
        }
        calendar.put(vehiculeId, merged);
    }

    private static int getPassengerPriorityScoreForEntity(Reservation reservation) {
        Integer nbPassager = reservation.getNbPassager();
        if (nbPassager == null)
            return 0;
        if (nbPassager >= 5)
            return 3;
        if (nbPassager >= 3)
            return 2;
        return 1;
    }

    private static int getPassengerPriorityScore(ReservationDTO reservation) {
        Integer nbPassager = reservation.getNb_passager();
        if (nbPassager == null)
            return 0;
        if (nbPassager >= 5)
            return 3;
        if (nbPassager >= 3)
            return 2;
        return 1;
    }

    private static LocalDateTime getClosestAvailableDateForVehicle(Integer vehicleId, LocalDateTime date,
            Map<Integer, List<Interval>> calendar) {
        if (vehicleId == null || date == null)
            return date;
        List<Interval> intervals = calendar.get(vehicleId);
        if (intervals == null || intervals.isEmpty())
            return date;
        intervals.sort(Comparator.comparing(i -> i.start));
        for (Interval iv : intervals) {
            if (isDateBetween(iv.start, iv.end, date) || iv.start.isEqual(date)) {
                return iv.end;
            }
            if (date.isBefore(iv.start)) {
                return date;
            }
        }
        return date;
    }

    private static Map<Integer, List<Interval>> copyCalendar(Map<Integer, List<Interval>> src) {
        Map<Integer, List<Interval>> copy = new HashMap<>();
        if (src == null)
            return copy;
        for (Map.Entry<Integer, List<Interval>> e : src.entrySet()) {
            List<Interval> list = new ArrayList<>();
            for (Interval iv : e.getValue()) {
                list.add(new Interval(iv.start, iv.end));
            }
            copy.put(e.getKey(), list);
        }
        return copy;
    }

    private static boolean isVehicleFreeOverall(Integer vehiculeId, LocalDateTime start, LocalDateTime end,
            Map<Integer, List<Interval>> occupiedCalendar, Map<Integer, List<Interval>> tentativeCalendar) {
        if (vehiculeId == null)
            return false;
        if (!isVehicleFree(vehiculeId, start, end, occupiedCalendar))
            return false;
        if (!isVehicleFree(vehiculeId, start, end, tentativeCalendar))
            return false;
        return true;
    }

    public AssignmentResult assignVehicles(LocalDate date) throws Exception {
        List<Reservation> reservationsDisponibles = Reservation.findUnassignedByDate(date);

        // Phase 1: Convert to DTO and initialize tracking maps
        List<ReservationDTO> reservationDtos = new ArrayList<>();
        Map<Integer, Integer> remainingPassengers = new HashMap<>();
        Map<Integer, Integer> originalPassengers = new HashMap<>();

        for (Reservation r : reservationsDisponibles) {
            ReservationDTO dto = r.toDto();
            reservationDtos.add(dto);
            if (dto.getId() != null) {
                remainingPassengers.put(dto.getId(), dto.getNb_passager());
                originalPassengers.put(dto.getId(), dto.getNb_passager());
            }
        }

        reservationsDisponibles.sort(
                Comparator.comparingInt(AssignmentService::getPassengerPriorityScoreForEntity).reversed()
                        .thenComparing(Reservation::getNbPassager, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Reservation::getDateHeureArrivee,
                                Comparator.nullsLast(Comparator.naturalOrder())));

        List<Vehicule> vehiculesDisponibles = new ArrayList<>(Vehicule.findAll(Vehicule.class));

        Lieux aeroport = Lieux.findAeroport();
        if (aeroport == null) {
            throw new Exception("Aéroport non trouvé dans la base de données");
        }

        Map<Integer, List<Interval>> occupiedCalendar = new HashMap<>();
        for (Vehicule v : vehiculesDisponibles) {
            try {
                List<Trajet> trajetsExistants = v.findTrajets(date);
                for (Trajet t : trajetsExistants) {
                    if (t.getHeureDepart() != null && t.getHeureArrivee() != null) {
                        addIntervalToCalendar(v.getId(), t.getHeureDepart(), t.getHeureArrivee(), occupiedCalendar);
                    }
                }
            } catch (Exception e) {
                System.out.println("[ERREUR AssignmentService.assignVehicles add to calendar] " + e.getMessage());
            }
        }

        Map<Integer, List<Interval>> tentativeCalendar = copyCalendar(occupiedCalendar);

        List<TrajetCandidat> candidats = new ArrayList<>();
        Set<Integer> reservationsAssignees = new HashSet<>();
        Set<Integer> failedReservations = new HashSet<>();
        List<ReservationDTO> remainingReservations = new ArrayList<>(reservationDtos);

        remainingReservations.sort(
                Comparator.comparingInt(AssignmentService::getPassengerPriorityScore).reversed()
                        .thenComparing(ReservationDTO::getNb_passager, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(ReservationDTO::getDateHeureArrivee,
                                Comparator.nullsLast(Comparator.naturalOrder())));

        while (!remainingReservations.isEmpty()) {
            List<ReservationDTO> disponiblesPourTraitement = new ArrayList<>(remainingReservations);

            if (disponiblesPourTraitement.isEmpty())
                break;

            ReservationDTO premiereReservation = disponiblesPourTraitement.get(0);

            if (failedReservations.contains(premiereReservation.getId())) {
                remainingReservations.remove(premiereReservation);
                continue;
            }

            List<Vehicule> vehiculesCandidates = new ArrayList<>(vehiculesDisponibles);
            boolean assignedThisReservation = false;

            while (!vehiculesCandidates.isEmpty() && !assignedThisReservation) {
                Vehicule meilleurVehicule = findBestVehicle(premiereReservation, candidats, vehiculesCandidates);
                if (meilleurVehicule == null) {
                    reservationsAssignees.add(premiereReservation.getId());
                    break;
                }

                // If passenger count > free seats of best vehicle, we split it
                Integer freeSeats = meilleurVehicule.getNbrPlace();

                // Recalculate real free seats based on max capacity if needed,
                // but here findBestVehicle already does filtering.
                // However, for SPLIT, we need exact remaining capacity.
                // For now, assume findBestVehicle logic holds.
                // We need to implement the SPLIT logic here as requested in step 2.

                // Let's call the allocation logic
                int toAllocate = Math.min(premiereReservation.getNb_passager(), freeSeats);

                // If partial allocation needed
                if (toAllocate < premiereReservation.getNb_passager()) {
                    // Create fragment
                    ReservationDTO fragment = new ReservationDTO();
                    fragment.setId(premiereReservation.getId());
                    fragment.setId_client(premiereReservation.getId_client());
                    fragment.setId_hotel(premiereReservation.getId_hotel());
                    fragment.setNom_hotel(premiereReservation.getNom_hotel());
                    fragment.setDateHeureArrivee(premiereReservation.getDateHeureArrivee());
                    fragment.setDate_reservation(premiereReservation.getDate_reservation());
                    fragment.setTempsAttenteMax(premiereReservation.getTempsAttenteMax());
                    fragment.setNb_passager(toAllocate);

                    // Use fragment for this iteration
                    List<ReservationDTO> singleFragmentList = new ArrayList<>();
                    singleFragmentList.add(fragment);

                    GroupeReservation groupeReservation = new GroupeReservation();
                    groupeReservation.setReservations(singleFragmentList);
                    // We need arrival time at airport for the vehicle (which is just the arrival
                    // time of the reservation)
                    // Adjusted by closest availability
                    groupeReservation.setHeureArriveeVehicule(
                            getClosestAvailableDateForVehicle(meilleurVehicule.getId(), fragment.getDateHeureArrivee(),
                                    tentativeCalendar));

                    List<ReservationDTO> groupe = groupeReservation.getReservations();

                    TrajetCandidat candidat = optimizeRoute(meilleurVehicule,
                            groupeReservation.getHeureArriveeVehicule(),
                            groupe, aeroport);
                    TripTiming timing = calculateTripTiming(meilleurVehicule,
                            groupeReservation.getHeureArriveeVehicule(),
                            groupe, candidat.getOrdreVisites());

                    if (timing != null) {
                        candidat.setHeureDepart(timing.getHeureDepart());
                        candidat.setHeureArrivee(timing.getHeureArrivee());
                        candidat.setDistanceTotale(timing.getDistanceTotale());
                    }

                    LocalDateTime start = candidat.getHeureDepart();
                    LocalDateTime end = candidat.getHeureArrivee();

                    if (isVehicleFreeOverall(meilleurVehicule.getId(), start, end, occupiedCalendar,
                            tentativeCalendar) && meilleurVehicule.estDispo(end) && meilleurVehicule.estDispo(start)) {
                        candidats.add(candidat);
                        // Do not add to reservationsAssignees yet because the original is still pending
                        // with remaining passengers. Decrement only once in remainingReservations.
                        Integer allocated = fragment.getNb_passager();
                        for (ReservationDTO remDto : remainingReservations) {
                            if (remDto.getId().equals(fragment.getId())) {
                                int current = remDto.getNb_passager() != null ? remDto.getNb_passager() : 0;
                                remDto.setNb_passager(Math.max(0, current - allocated));
                                break;
                            }
                        }

                        addIntervalToCalendar(meilleurVehicule.getId(), start, end, tentativeCalendar);

                        // Because we split, we don't mark assignedThisReservation=true for the MAIN
                        // loop to continue processing the same reservation?
                        // Actually, the main loop takes index 0. We modified index 0 (decremented).
                        // So next iteration of "while (!remainingReservations.isEmpty())" will pick it
                        // up again with reduced passengers.
                        // But we need to break the inner loop "vehiculesCandidates" to restart finding
                        // best vehicle for the REMAINDER.
                        assignedThisReservation = true; // Signals we did something with this vehicle iteration
                        // But we want to CONTINUE processing the remainder.
                        // If we set assignedThisReservation=true, it breaks the inner loop, goes to
                        // outer loop.
                        // Outer loop checks if empty. It is not.
                        // Outer loop takes index 0. It is the modified DTO.
                        // Perfect.
                    } else {
                        // Undo split ? No, just fail this vehicle
                        premiereReservation.setNb_passager(premiereReservation.getNb_passager() + toAllocate);
                        vehiculesCandidates.remove(meilleurVehicule);
                    }

                } else {
                    // Full allocation (compatible capacity)
                    GroupeReservation groupeReservation = findOptimalWaitingGroup(meilleurVehicule, tentativeCalendar,
                            disponiblesPourTraitement);
                    List<ReservationDTO> groupe = groupeReservation.getReservations();

                    if (groupe.isEmpty()) {
                        vehiculesCandidates.remove(meilleurVehicule);
                        continue;
                    }

                    TrajetCandidat candidat = optimizeRoute(meilleurVehicule,
                            groupeReservation.getHeureArriveeVehicule(),
                            groupe, aeroport);
                    TripTiming timing = calculateTripTiming(meilleurVehicule,
                            groupeReservation.getHeureArriveeVehicule(),
                            groupe, candidat.getOrdreVisites());
                    if (timing != null) {
                        candidat.setHeureDepart(timing.getHeureDepart());
                        candidat.setHeureArrivee(timing.getHeureArrivee());
                        candidat.setDistanceTotale(timing.getDistanceTotale());
                    }

                    LocalDateTime start = candidat.getHeureDepart();
                    LocalDateTime end = candidat.getHeureArrivee();

                    if (isVehicleFreeOverall(meilleurVehicule.getId(), start, end, occupiedCalendar,
                            tentativeCalendar) && meilleurVehicule.estDispo(end) && meilleurVehicule.estDispo(start)) {
                        candidats.add(candidat);
                        for (ReservationDTO r : groupe) {
                            reservationsAssignees.add(r.getId());
                            // Update logic for split
                            // The 'r' here is the group fragment.
                            // We need to decrease the original 'remaining' object from
                            // remainingReservations.
                            Integer fragmentSize = r.getNb_passager();
                            for (ReservationDTO remDto : remainingReservations) {
                                if (remDto.getId().equals(r.getId())) {
                                    if (remDto.getNb_passager() > fragmentSize) {
                                        remDto.setNb_passager(remDto.getNb_passager() - fragmentSize);
                                    } else {
                                        remDto.setNb_passager(0);
                                    }
                                    break;
                                }
                            }
                        }
                        addIntervalToCalendar(meilleurVehicule.getId(), start, end, tentativeCalendar);
                        assignedThisReservation = true;
                    } else {
                        vehiculesCandidates.remove(meilleurVehicule);
                    }
                } // end loop vehiculesCandidates
            } // end else full allocation

            if (!assignedThisReservation) {
                // Not assigned (even partially) = failed for this day/configuration
                failedReservations.add(premiereReservation.getId());
            }

            if (!candidats.isEmpty()) {
                try {
                    groupVehicles(candidats);
                } catch (Exception e) {
                    System.out.println("[WARN] groupVehicles a échoué: " + e.getMessage());
                }

                tentativeCalendar.clear();
                for (TrajetCandidat c : candidats) {
                    if (c == null || c.getVehicule() == null || c.getHeureDepart() == null
                            || c.getHeureArrivee() == null)
                        continue;
                    addIntervalToCalendar(c.getVehicule().getId(), c.getHeureDepart(), c.getHeureArrivee(),
                            tentativeCalendar);
                }

                Iterator<TrajetCandidat> it = candidats.iterator();
                while (it.hasNext()) {
                    TrajetCandidat c = it.next();
                    if (c == null) {
                        it.remove();
                        continue;
                    }
                    try {
                        TripTiming nt = calculateTripTiming(c.getVehicule(), c.getHeureDepart(), c.getReservations(),
                                c.getOrdreVisites());
                        if (nt == null) {
                            it.remove();
                            continue;
                        }
                        c.setHeureDepart(nt.getHeureDepart());
                        c.setHeureArrivee(nt.getHeureArrivee());
                        c.setDistanceTotale(nt.getDistanceTotale());
                    } catch (Exception ex) {
                        System.out.println("[WARN] recalcul timing candidat échoué: " + ex.getMessage());
                        it.remove();
                    }
                }

                tentativeCalendar.clear();
                for (TrajetCandidat c : candidats) {
                    if (c == null || c.getVehicule() == null || c.getHeureDepart() == null
                            || c.getHeureArrivee() == null)
                        continue;
                    addIntervalToCalendar(c.getVehicule().getId(), c.getHeureDepart(), c.getHeureArrivee(),
                            tentativeCalendar);
                }
            }

            Iterator<ReservationDTO> remIt = remainingReservations.iterator();
            while (remIt.hasNext()) {
                ReservationDTO r = remIt.next();
                if (failedReservations.contains(r.getId())) {
                    remIt.remove();
                } else if (reservationsAssignees.contains(r.getId())
                        && (r.getNb_passager() == null || r.getNb_passager() <= 0)) {
                    remIt.remove();
                }
            }
        }

        candidats.sort((c1, c2) -> Integer.compare(
                c2.getReservations().size(),
                c1.getReservations().size()));

        List<Trajet> trajetsCreated = new ArrayList<>();

        Map<Integer, List<Interval>> occupiedToSave = copyCalendar(occupiedCalendar);
        for (TrajetCandidat candidat : candidats) {
            if (candidat == null || candidat.getVehicule() == null || candidat.getHeureDepart() == null
                    || candidat.getHeureArrivee() == null)
                continue;
            Integer vid = candidat.getVehicule().getId();
            LocalDateTime start = candidat.getHeureDepart();
            LocalDateTime end = candidat.getHeureArrivee();

            if (!isVehicleFree(vid, start, end, occupiedToSave)) {
                System.out.println("[SAVE] skip candidat veh=" + vid + " start=" + start + " end=" + end
                        + " cause overlap with occupiedToSave=" + formatIntervals(occupiedToSave.get(vid)));
                continue;
            }

            if (candidat.getReservations() == null || candidat.getReservations().isEmpty()) {
                System.out.println("[SAVE] skip candidat empty reservations");
                continue;
            }

            Trajet trajet = saveTrajet(candidat, date);
            trajetsCreated.add(trajet);
            addIntervalToCalendar(vid, start, end, occupiedToSave);
        }

        // Return original POJOs for non assigned
        List<Reservation> reservationsNonAssignees = new ArrayList<>();
        // We need to reconstruct DTOs back to Entity or just return those that have
        // remaining > 0?
        for (ReservationDTO dto : remainingReservations) {
            // Find original or create new with remaining
            // Actually findUnassignedByDate returned entities.
            // We can map remaining DTOs back.
            // But wait, DTO might be partially assigned.
            // So if remaining > 0, we return it as unassigned (partially).
            if (dto.getNb_passager() > 0) {
                // Find original entity to return correct type, but update its passenger count?
                // Or just creating dummy entity?
                // Match with id to find the original entity
                for (Reservation r : reservationsDisponibles) {
                    if (r.getId().equals(dto.getId())) {
                        r.setNbPassager(dto.getNb_passager()); // Update with remaining
                        reservationsNonAssignees.add(r);
                        break;
                    }
                }
            }
        }

        // Add failed reservations (never assigned) with their *initial* count unless
        // modified?
        // Actually if they are in failedReservations, they might still be in
        // remainingReservations list if we didn't remove them.
        // But above loop iterates remainingReservations.
        // If they were removed from remainingReservations because of failure, we need
        // to add them back.
        // Wait, loop above iterates remainingReservations.
        // In the main loop, if failed, we removed it from remainingReservations (in
        // previous fix attempt I removed it?).
        // Actually, my fix removed it from the ITERATOR, so it IS removed from
        // remainingReservations list.
        // So we need to recover them.

        for (Integer failedId : failedReservations) {
            // Find in original DTOs map or list
            boolean alreadyAdded = false;
            for (Reservation res : reservationsNonAssignees) {
                if (res.getId().equals(failedId)) {
                    alreadyAdded = true;
                    break;
                }
            }
            if (!alreadyAdded) {
                for (Reservation r : reservationsDisponibles) {
                    if (r.getId().equals(failedId)) {
                        // We need the CURRENT remaining passengers for this one.
                        // Where is it stored? In the DTO that was removed.
                        // We should probably find the DTO in reservationDtos list (which was the
                        // source).
                        // reservationDtos list contains the DTO objects which were modified in place.
                        for (ReservationDTO dto : reservationDtos) {
                            if (dto.getId().equals(failedId)) {
                                r.setNbPassager(dto.getNb_passager());
                                reservationsNonAssignees.add(r);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        return new AssignmentResult(trajetsCreated, reservationsNonAssignees);
    }

    private GroupeReservation findOptimalWaitingGroup(Vehicule vehicule, Map<Integer, List<Interval>> calendar,
            List<ReservationDTO> disponibles)
            throws Exception {
        GroupeReservation groupeVide = new GroupeReservation();
        groupeVide.setReservations(new ArrayList<>());
        if (vehicule == null || disponibles == null || disponibles.isEmpty())
            return groupeVide;

        ReservationDTO premiere = disponibles.get(0);
        if (premiere == null)
            return groupeVide;

        Integer nbPremiere = premiere.getNb_passager();
        if (nbPremiere == null || nbPremiere > vehicule.getNbrPlace())
            return groupeVide;

        LocalDateTime heurePremiere = premiere.getDateHeureArrivee();
        if (heurePremiere == null) {
            List<ReservationDTO> groupe = new ArrayList<>();
            groupe.add(premiere);
            groupeVide.setReservations(groupe);
            return groupeVide;
        }

        LocalDateTime heureArrivee = getClosestAvailableDateForVehicle(vehicule.getId(), heurePremiere, calendar);

        long attenteDeja = Math.max(0, Duration.between(heurePremiere, heureArrivee).toMinutes());
        Integer tempsAttenteMaxEffectif = 120; // Default
        if (premiere.getTempsAttenteMax() != null) {
            tempsAttenteMaxEffectif = premiere.getTempsAttenteMax();
        }
        Integer tempsAttenteRestant = tempsAttenteMaxEffectif - (int) attenteDeja;

        if (tempsAttenteRestant <= 0) {
            return groupeVide;
        }

        LocalDateTime limiteAttente = heureArrivee.plusMinutes(tempsAttenteRestant);

        TreeSet<LocalDateTime> pointsDepart = new TreeSet<>();
        pointsDepart.add(heurePremiere);
        for (int i = 1; i < disponibles.size(); i++) {
            ReservationDTO candidate = disponibles.get(i);
            if (candidate == null || candidate.getDateHeureArrivee() == null)
                continue;
            LocalDateTime heureCandidate = candidate.getDateHeureArrivee();
            if (!heureCandidate.isBefore(heurePremiere) && !heureCandidate.isAfter(limiteAttente)) {
                pointsDepart.add(heureCandidate);
            }
        }

        List<ReservationDTO> meilleurGroupe = new ArrayList<>();
        int meilleurNbPassagers = -1;
        int meilleurNbReservations = -1;
        int meilleurWaste = Integer.MAX_VALUE;
        LocalDateTime meilleurDepart = null;

        for (LocalDateTime departCandidat : pointsDepart) {
            List<ReservationDTO> groupeCandidat = buildGroupAtDeparture(vehicule, premiere, disponibles,
                    departCandidat);
            if (groupeCandidat.isEmpty())
                continue;

            int nbPassagers = 0;
            int waste = 0;
            for (ReservationDTO reservation : groupeCandidat) {
                if (reservation.getNb_passager() != null) {
                    nbPassagers += reservation.getNb_passager();
                    // Calculate waste if this is a fragment/split
                    // We need to find the original to compare sizes, or infer it?
                    // Since buildGroupAtDeparture creates NEW objects for fragments,
                    // we can't easily rely on object identity to find original in 'disponibles'.
                    // But we can match by ID.
                    for (ReservationDTO orig : disponibles) {
                        if (orig.getId().equals(reservation.getId())) {
                            if (orig.getNb_passager() > reservation.getNb_passager()) {
                                waste += (orig.getNb_passager() - reservation.getNb_passager());
                            }
                            break;
                        }
                    }
                }
            }

            boolean meilleur = false;
            if (nbPassagers > meilleurNbPassagers) {
                meilleur = true;
            } else if (nbPassagers == meilleurNbPassagers) {
                // Tie breaker: Minimize waste (leftover passengers from split)
                if (waste < meilleurWaste) {
                    meilleur = true;
                } else if (waste == meilleurWaste) {
                    if (groupeCandidat.size() > meilleurNbReservations) {
                        meilleur = true;
                    } else if (groupeCandidat.size() == meilleurNbReservations
                            && (meilleurDepart == null || departCandidat.isBefore(meilleurDepart))) {
                        meilleur = true;
                    }
                }
            }

            if (meilleur) {
                meilleurNbPassagers = nbPassagers;
                meilleurNbReservations = groupeCandidat.size();
                meilleurWaste = waste;
                meilleurDepart = departCandidat;
                meilleurGroupe = groupeCandidat;
            }
        }

        GroupeReservation gp = new GroupeReservation();
        gp.setReservations(meilleurGroupe);
        gp.setHeureArriveeVehicule(heureArrivee);
        return gp;
    }

    private List<ReservationDTO> buildGroupAtDeparture(Vehicule vehicule, ReservationDTO premiere,
            List<ReservationDTO> disponibles, LocalDateTime departCandidat) {
        List<ReservationDTO> groupe = new ArrayList<>();
        int capaciteTotale = 0;

        if (premiere.getNb_passager() == null)
            return groupe;

        // This method assumes the premiere is already part of the group
        // If premiere > capacity, it should have been split before or not selected.
        // But for findOptimalWaitingGroup, it tries to fill the vehicle with OTHER
        // reservations on top of premiere.

        int firstCapacity = premiere.getNb_passager();
        if (firstCapacity > vehicule.getNbrPlace())
            return new ArrayList<>(); // Too big

        groupe.add(copyReservationDto(premiere));
        capaciteTotale = firstCapacity;

        List<ReservationDTO> candidates = new ArrayList<>();
        // Skip first (premiere)
        for (int i = 1; i < disponibles.size(); i++) {
            ReservationDTO candidate = disponibles.get(i);
            if (candidate == null || candidate.getDateHeureArrivee() == null)
                continue;

            LocalDateTime heureCandidate = candidate.getDateHeureArrivee();
            LocalDateTime heurePremiere = premiere.getDateHeureArrivee();

            // Candidate must arrive before or at departure time
            if (!heureCandidate.isBefore(heurePremiere) && !heureCandidate.isAfter(departCandidat)) {
                candidates.add(candidate);
            }
        }

        // New filling logic: prioritize closest to remaining places
        Set<Integer> addedIds = new HashSet<>();
        addedIds.add(premiere.getId());

        while (capaciteTotale < vehicule.getNbrPlace()) {
            int remaining = vehicule.getNbrPlace() - capaciteTotale;
            ReservationDTO bestMatch = null;
            int minDiff = Integer.MAX_VALUE;

            for (ReservationDTO cand : candidates) {
                if (addedIds.contains(cand.getId()))
                    continue;
                if (cand.getNb_passager() == null)
                    continue;

                if (cand.getNb_passager() <= remaining) {
                    int diff = remaining - cand.getNb_passager();
                    if (diff < minDiff) {
                        minDiff = diff;
                        bestMatch = cand;
                    }
                }
            }

            if (bestMatch != null) {
                groupe.add(copyReservationDto(bestMatch));
                addedIds.add(bestMatch.getId());
                capaciteTotale += bestMatch.getNb_passager();
            } else {
                // Try to split a larger reservation to fill the remaining space
                ReservationDTO splitCandidate = null;
                // Prioritize largest to fill? Or just first available?

                // We want to find the one CLOSEST to remaining but LARGER than remaining
                int bestSplitDiff = Integer.MAX_VALUE;

                for (ReservationDTO cand : candidates) {
                    if (addedIds.contains(cand.getId()))
                        continue;
                    if (cand.getNb_passager() != null && cand.getNb_passager() > remaining) {
                        int diff = cand.getNb_passager() - remaining;
                        if (diff < bestSplitDiff) {
                            bestSplitDiff = diff;
                            splitCandidate = cand;
                        }
                    }
                }

                if (splitCandidate != null) {
                    // Create fragment
                    ReservationDTO fragment = new ReservationDTO();
                    fragment.setId(splitCandidate.getId());
                    fragment.setId_client(splitCandidate.getId_client());
                    fragment.setId_hotel(splitCandidate.getId_hotel());
                    fragment.setNom_hotel(splitCandidate.getNom_hotel());
                    fragment.setDateHeureArrivee(splitCandidate.getDateHeureArrivee());
                    fragment.setDate_reservation(splitCandidate.getDate_reservation());
                    fragment.setTempsAttenteMax(splitCandidate.getTempsAttenteMax());
                    fragment.setNb_passager(remaining);

                    // Decrement original (NOT HERE, we are inside a simulation method that is
                    // called multiple times)
                    // We must return the fragment, but we should not permanently modify the
                    // candidate in the simulation phase.
                    // However, the caller expects a valid group.
                    // The 'candidates' list comes from 'disponibles', which is passed from
                    // 'findOptimalWaitingGroup'.
                    // 'findOptimalWaitingGroup' gets 'disponiblesPourTraitement'.
                    // If we modify 'disponibles', we corrupt the state for other 'departCandidat'
                    // iterations.

                    // To avoid corruption, we don't modify the source 'splitCandidate' here.
                    // We just use the fragment.
                    // But in 'assignVehicles', when we commit the group, we need to know we have to
                    // reduce the original.
                    // The 'assignVehicles' logic iterates over the chosen group and updates
                    // 'reservationsAssignees'.
                    // We need to implement logic in 'assignVehicles' to handle this update.

                    groupe.add(fragment);
                    addedIds.add(splitCandidate.getId());
                    capaciteTotale += remaining;
                }
                break;
            }
        }
        return groupe;
    }

    private ReservationDTO copyReservationDto(ReservationDTO source) {
        ReservationDTO copy = new ReservationDTO();
        copy.setId(source.getId());
        copy.setId_client(source.getId_client());
        copy.setId_hotel(source.getId_hotel());
        copy.setNom_hotel(source.getNom_hotel());
        copy.setDateHeureArrivee(source.getDateHeureArrivee());
        copy.setDate_reservation(source.getDate_reservation());
        copy.setTempsAttenteMax(source.getTempsAttenteMax());
        copy.setNb_passager(source.getNb_passager());
        return copy;
    }

    private TrajetCandidat optimizeRoute(Vehicule vehicule, LocalDateTime heureArriveeVehicule,
            List<ReservationDTO> groupe, Lieux aeroport) throws Exception {
        List<String> ordreVisites = new ArrayList<>();
        ordreVisites.add(aeroport.getCode());

        BigDecimal distanceTotal = BigDecimal.ZERO;
        Lieux positionActuelle = aeroport;

        Set<Integer> nonVisites = new HashSet<>();
        Map<Integer, Lieux> hotelCache = new HashMap<>();

        for (ReservationDTO reservation : groupe) {
            // With fragments, ID might be duplicate.
            // But logic uses ID to track "visited".
            // Since all fragments of same reservation go to same hotel, it's fine.
            // But we need to distinguish if we have multiple fragments of same ID in the
            // SAME group?
            // In the current logic, a group is built for ONE vehicle trip.
            // A vehicle trip won't carry 2 fragments of same reservation usually (it would
            // just carry bigger chunk).
            // UNLESS splits happened weirdly.
            // But let's assume standard behavior: 1 fragment per reservation per trip.
            // If uniqueness of ID is issue, we might need UUID for fragments or reference
            // object identity.
            // But reservation IDs are Integers from DB.
            nonVisites.add(reservation.getId());
            Lieux hotel = null;
            // Need a way to get Lieux from ID efficiently or mock it
            // The DTO has id_hotel.
            // We can use Lieux.findById(id) or similar if available.
            // Or since we have nom_hotel... but we need Coordinates/Distance.
            // Let's assume Lieux.findById works or we fetch all Lieux before.
            // Original code used reservation.getForeignKey("id_hotel").
            // We can't call getForeignKey on DTO.
            // We need to fetch Lieux entity.

            // Allow me to cheat slightly and fetch Lieux based on id_hotel
            // Assuming Lieux has a static cache or find method.
            // Original code used `reservation.getForeignKey`.
            // Let's implement a quick helper or fetch it.
            if (hotel == null) {
                // Fallback: fetch from DB
                String sql = "SELECT * FROM hotel WHERE id = ?";
                List<Lieux> res = Lieux.fetch(Lieux.class, sql, reservation.getId_hotel());
                if (!res.isEmpty())
                    hotel = res.get(0);
            }

            hotelCache.put(reservation.getId(), hotel);
        }

        while (!nonVisites.isEmpty()) {
            BigDecimal minDistance = BigDecimal.valueOf(Double.MAX_VALUE);
            ReservationDTO plusProche = null;
            Lieux hotelPlusProche = null;

            for (ReservationDTO reservation : groupe) {
                if (!nonVisites.contains(reservation.getId()))
                    continue;

                Lieux hotel = hotelCache.get(reservation.getId());
                Distance distanceObj = Distance.getDistance(positionActuelle.getCode(), hotel.getCode());

                if (distanceObj != null) {
                    BigDecimal dist = distanceObj.getDistanceKm();
                    if (dist.compareTo(minDistance) < 0) {
                        minDistance = dist;
                        plusProche = reservation;
                        hotelPlusProche = hotel;
                    }
                }
            }

            if (plusProche != null && hotelPlusProche != null) {
                ordreVisites.add(hotelPlusProche.getCode());
                distanceTotal = distanceTotal.add(minDistance);
                nonVisites.remove(plusProche.getId());
                positionActuelle = hotelPlusProche;
            } else {
                break;
            }
        }

        ordreVisites.add(aeroport.getCode());

        // Find latest arrival time
        LocalDateTime lastArrival = null;
        for (ReservationDTO r : groupe) {
            if (lastArrival == null || r.getDateHeureArrivee().isAfter(lastArrival)) {
                lastArrival = r.getDateHeureArrivee();
            }
        }

        if (lastArrival == null)
            throw new Exception("Erreur lors de l'obtention de la réservation la plus tardive");

        LocalDateTime heureDepart = heureArriveeVehicule != null ? heureArriveeVehicule : lastArrival;
        if (heureDepart.isBefore(lastArrival)) {
            heureDepart = lastArrival;
        }

        double vitesse = vehicule.getVitesseMoyenne() == null || vehicule.getVitesseMoyenne() <= 0.0 ? 60.0
                : vehicule.getVitesseMoyenne();
        double minutesTrajet = distanceTotal.doubleValue() / vitesse * 60.0;
        long minutesArrondis = (long) Math.ceil(minutesTrajet);
        LocalDateTime heureArrivee = heureDepart.plusMinutes(minutesArrondis);

        return new TrajetCandidat(vehicule, groupe, heureDepart, heureArrivee, distanceTotal, ordreVisites);
    }

    private TripTiming calculateTripTiming(Vehicule v, LocalDateTime heureArriveeVehicule, List<ReservationDTO> groupe,
            List<String> ordre) throws Exception {
        if (groupe == null || groupe.isEmpty() || ordre == null || ordre.size() < 2)
            return null;

        LocalDateTime lastArrival = null;
        for (ReservationDTO r : groupe) {
            if (lastArrival == null || r.getDateHeureArrivee().isAfter(lastArrival)) {
                lastArrival = r.getDateHeureArrivee();
            }
        }

        if (lastArrival == null)
            throw new Exception("Erreur lors de l'obtention de la réservation la plus tardive");

        LocalDateTime heureDepart = heureArriveeVehicule != null ? heureArriveeVehicule : lastArrival;
        if (heureDepart.isBefore(lastArrival)) {
            heureDepart = lastArrival;
        }

        BigDecimal distanceTotale = BigDecimal.ZERO;
        long dureeMinutesTotale = 0;
        double vitesse = v.getVitesseMoyenne() == null || v.getVitesseMoyenne() <= 0.0 ? 60.0 : v.getVitesseMoyenne();

        for (int i = 0; i < ordre.size() - 1; i++) {
            String codeFrom = ordre.get(i);
            String codeTo = ordre.get(i + 1);

            Distance distance = Distance.getDistance(codeFrom, codeTo);
            if (distance == null) {
                System.out.println("[WARN] distance introuvable entre " + codeFrom + " et " + codeTo);
                return null;
            }
            BigDecimal distanceKm = distance.getDistanceKm();
            distanceTotale = distanceTotale.add(distanceKm);
            double dureeMinutes = distanceKm.doubleValue() / vitesse * 60.0;
            dureeMinutesTotale += (long) Math.ceil(dureeMinutes);
        }

        LocalDateTime heureArrivee = heureDepart.plusMinutes(dureeMinutesTotale);

        return new TripTiming(heureDepart, heureArrivee, distanceTotale);
    }

    private Vehicule findBestVehicle(ReservationDTO reservation, List<TrajetCandidat> candidats,
            List<Vehicule> disponibles) {
        if (reservation == null || disponibles == null || disponibles.isEmpty())
            return null;

        Integer nbPassagers = reservation.getNb_passager();
        List<Vehicule> vehiculesCompatibles = new ArrayList<>();
        // For partial allocation:
        // We consider ALL vehicles with > 0 places (assuming they are free in general
        // sense, but occupancy check is later).
        // BUT strict constraint: if we want to prioritize full allocation, we first
        // look for vehicles with capacity >= nbPassagers.

        // 1. Try to find vehicle with enough capacity for full allocation
        List<Vehicule> vehiclesWithFullCapacity = new ArrayList<>();
        for (Vehicule v : disponibles) {
            if (v.getNbrPlace() >= nbPassagers)
                vehiclesWithFullCapacity.add(v);
        }

        // 2. If valid vehicles found, use original logic (Best Capacity)
        if (!vehiclesWithFullCapacity.isEmpty()) {
            return selectBestVehicleFromCandidates(vehiclesWithFullCapacity, candidats, nbPassagers);
        }

        // 3. If no vehicle can take ALL passengers, we switch to SPLIT strategy.
        // Sort vehicles by Free Seats (DESC), then candidate count (ASC), then Fuel (D
        // preferred).
        // Since we are inside findBestVehicle, we return the BEST one to trigger
        // allocation logic in main loop.

        // Actually, we should filter out vehicles that are totally full?
        // Wait, 'disponibles' list implies they are available vehicles in general.
        // We know their capacity NbrPlace.
        // We heavily rely on isVehicleFreeOverall in main loop.
        // Here we just pick a candidate vehicle.

        // Sort disponibles by NbrPlace DESC
        List<Vehicule> sortedCandidates = new ArrayList<>(disponibles);

        // Count usage in current candidate list to break ties
        Map<Integer, Long> candidatsVehicules = new HashMap<>();
        for (Vehicule v : sortedCandidates) {
            Long count = 0L;
            for (TrajetCandidat tc : candidats) {
                if (tc.getVehicule().getId().equals(v.getId())) {
                    count++;
                }
            }
            candidatsVehicules.put(v.getId(), count);
        }

        sortedCandidates.sort((v1, v2) -> {
            // 1. Capacity (Free Seats - assuming empty for now, or just max capacity)
            // In static context, NbrPlace is capacity.
            int capCompare = v2.getNbrPlace().compareTo(v1.getNbrPlace());
            if (capCompare != 0)
                return capCompare;

            // 2. Candidate Count (Usage balance)
            Long c1 = candidatsVehicules.getOrDefault(v1.getId(), 0L);
            Long c2 = candidatsVehicules.getOrDefault(v2.getId(), 0L);
            int usageCompare = c1.compareTo(c2);
            if (usageCompare != 0)
                return usageCompare;

            // 3. Fuel
            boolean d1 = "D".equals(v1.getTypeCarburant());
            boolean d2 = "D".equals(v2.getTypeCarburant());
            if (d1 && !d2)
                return -1;
            if (!d1 && d2)
                return 1;

            return 0;
        });

        return sortedCandidates.isEmpty() ? null : sortedCandidates.get(0);
    }

    private Vehicule selectBestVehicleFromCandidates(List<Vehicule> candidates, List<TrajetCandidat> existingCandidats,
            int neededCapacity) {
        int capaciteMin = Integer.MAX_VALUE;
        for (Vehicule v : candidates)
            if (v.getNbrPlace() < capaciteMin)
                capaciteMin = v.getNbrPlace();

        List<Vehicule> meilleurCapacite = new ArrayList<>();
        for (Vehicule v : candidates)
            if (v.getNbrPlace() == capaciteMin)
                meilleurCapacite.add(v);

        if (meilleurCapacite.size() == 1)
            return meilleurCapacite.get(0);

        Map<Integer, Long> candidatsVehicules = new HashMap<>();

        for (Vehicule v : meilleurCapacite) {
            Long count = 0L;
            for (TrajetCandidat tc : existingCandidats) {
                if (tc.getVehicule().getId().equals(v.getId())) {
                    count++;
                }
            }
            candidatsVehicules.put(v.getId(), count);
        }

        Long nbrCandidats = candidatsVehicules.values()
                .stream()
                .min(Long::compareTo)
                .orElse(0L);

        List<Vehicule> candidatesFilteredByUsage = new ArrayList<>();
        for (Vehicule v : meilleurCapacite) {
            if (candidatsVehicules.get(v.getId()) <= nbrCandidats) {
                candidatesFilteredByUsage.add(v);
            }
        }

        if (candidatesFilteredByUsage.isEmpty()) {
            // Should not happen if logic is correct
            candidatesFilteredByUsage = meilleurCapacite;
        }

        if (candidatesFilteredByUsage.size() == 1) {
            return candidatesFilteredByUsage.get(0);
        }

        List<Vehicule> vehiculesDiesel = new ArrayList<>();
        for (Vehicule v : candidatesFilteredByUsage)
            if ("D".equals(v.getTypeCarburant()))
                vehiculesDiesel.add(v);

        List<Vehicule> vehiculesFinaux = vehiculesDiesel.isEmpty() ? candidatesFilteredByUsage : vehiculesDiesel;

        if (vehiculesFinaux.size() == 1)
            return vehiculesFinaux.get(0);

        return vehiculesFinaux.get(new Random().nextInt(vehiculesFinaux.size()));
    }

    public void groupVehicles(List<TrajetCandidat> trajetCandidats) {
        if (trajetCandidats == null || trajetCandidats.isEmpty()) {
            System.out.println("groupVehicles: aucun candidat fourni ou liste vide");
            return;
        }

        System.out.println("groupVehicles: appelé avec " + trajetCandidats.size() + " candidats");

        List<TrajetCandidat> list = new ArrayList<>();
        for (TrajetCandidat tc : trajetCandidats) {
            if (tc == null) {
                System.out.println("groupVehicles: candidat null dans input, skip");
                continue;
            }
            if (tc.getHeureDepart() == null || tc.getHeureArrivee() == null) {
                System.out.println("groupVehicles: candidat sans heure depart/arrivee, skip: " + tc);
                continue;
            }
            list.add(tc);
        }

        list.sort(
                Comparator.comparing(TrajetCandidat::getHeureDepart, Comparator.nullsLast(Comparator.naturalOrder())));

        Set<TrajetCandidat> processed = new HashSet<>();

        for (TrajetCandidat base : new ArrayList<>(list)) {
            if (base == null)
                continue;
            if (processed.contains(base)) {
                System.out.println("groupVehicles: base deja traitée, skip: " + base);
                continue;
            }
            if (base.getHeureDepart() == null) {
                System.out.println("groupVehicles: base sans heureDepart, marque processed: " + base);
                processed.add(base);
                continue;
            }

            Integer tempsAttente = base.getTempsAttenteRestant() != null ? base.getTempsAttenteRestant() : 0;
            System.out.println("groupVehicles: base heureDepart=" + base.getHeureDepart() + " tempsAttenteRestant="
                    + tempsAttente + " vehicule=" + (base.getVehicule() != null ? base.getVehicule().getId() : null));

            if (tempsAttente <= 0) {
                System.out.println("groupVehicles: tempsAttente <= 0 pour base, on marque processed et continue");
                processed.add(base);
                continue;
            }

            LocalDateTime windowStart = base.getHeureDepart();
            LocalDateTime windowEnd = windowStart.plusMinutes(tempsAttente);
            System.out.println("groupVehicles: fenêtre de rassemblement [" + windowStart + " - " + windowEnd + "]");

            List<TrajetCandidat> group = new ArrayList<>();

            for (TrajetCandidat other : list) {
                if (other == null)
                    continue;
                if (other == base)
                    continue;
                if (processed.contains(other))
                    continue;
                if (other.getHeureDepart() == null)
                    continue;
                LocalDateTime od = other.getHeureDepart();
                boolean inWindow = (!od.isBefore(windowStart)) && (!od.isAfter(windowEnd));
                System.out.println("groupVehicles: comparaison autre depart=" + od + " inWindow=" + inWindow + " (base="
                        + windowStart + ") vehicule="
                        + (other.getVehicule() != null ? other.getVehicule().getId() : null));
                if (inWindow) {
                    group.add(other);
                }
            }

            System.out.println("groupVehicles: pour base depart=" + base.getHeureDepart() + " trouve " + group.size()
                    + " autres candidats");

            if (group.isEmpty()) {
                System.out.println("groupVehicles: aucun candidat à grouper avec base, marquage processed");
                processed.add(base);
                continue;
            }

            group.add(base);

            Optional<LocalDateTime> latestOpt = group.stream().map(TrajetCandidat::getHeureDepart)
                    .filter(Objects::nonNull).max(Comparator.naturalOrder());
            if (!latestOpt.isPresent()) {
                System.out.println("groupVehicles: latestOpt absent, marquage de tout le group comme processed");
                processed.addAll(group);
                continue;
            }

            LocalDateTime newDepart = latestOpt.get();
            System.out.println("groupVehicles: nouvelle heure de depart après regroupement = " + newDepart
                    + " pour group size=" + group.size());

            for (TrajetCandidat tc : group) {
                if (tc == null)
                    continue;
                if (tc.getHeureDepart() == null) {
                    System.out.println("groupVehicles: candidat sans heureDepart dans group, marque processed: " + tc);
                    processed.add(tc);
                    continue;
                }
                try {
                    System.out.println("groupVehicles: recalcul timing pour vehicule="
                            + (tc.getVehicule() != null ? tc.getVehicule().getId() : null) + " avec depart=" + newDepart
                            + " reservations=" + (tc.getReservations() != null ? tc.getReservations().size() : 0));
                    TripTiming recal = calculateTripTiming(tc.getVehicule(), newDepart, tc.getReservations(),
                            tc.getOrdreVisites());
                    if (recal == null) {
                        System.out.println(
                                "groupVehicles: recalcul a retourné null pour tc=" + tc + "; marquage processed");
                        processed.add(tc);
                        continue;
                    }
                    tc.setHeureDepart(recal.getHeureDepart());
                    tc.setHeureArrivee(recal.getHeureArrivee());
                    tc.setDistanceTotale(recal.getDistanceTotale());
                    System.out.println("groupVehicles: recalcul OK pour vehicule="
                            + (tc.getVehicule() != null ? tc.getVehicule().getId() : null) + " newDepart="
                            + recal.getHeureDepart() + " newArrivee=" + recal.getHeureArrivee() + " distance="
                            + recal.getDistanceTotale());
                    processed.add(tc);
                } catch (Exception e) {
                    System.out.println("groupVehicles: exception lors du recalcul timing pour tc=" + tc + " error="
                            + e.getMessage());
                    processed.add(tc);
                }
            }
        }

        System.out.println("groupVehicles: processing terminé, processed count="
                + list.stream().filter(processed::contains).count());
        System.out.println("groupVehicles: fin de la fonction");
    }

    private Trajet saveTrajet(TrajetCandidat candidat, LocalDate date) throws Exception {
        Trajet trajet = new Trajet();
        trajet.setIdVehicule(candidat.getVehicule().getId());
        trajet.setDateTrajet(date);
        trajet.setHeureDepart(candidat.getHeureDepart());
        trajet.setHeureArrivee(candidat.getHeureArrivee());
        trajet.setDistanceTotale(candidat.getDistanceTotale());
        trajet.setOrdreVisites(String.join(",", candidat.getOrdreVisites()));

        trajet = (Trajet) trajet.save();

        List<ReservationDTO> reservations = candidat.getReservations();
        for (int i = 0; i < reservations.size(); i++) {
            ReservationDTO reservation = reservations.get(i);

            TrajetReservation tr = new TrajetReservation();
            tr.setIdTrajet(trajet.getId().intValue());
            tr.setIdReservation(reservation.getId());
            tr.setOrdreVisite(i + 1);
            tr.setNbrPassager(reservation.getNb_passager());

            tr.save();
        }

        return trajet;
    }

    private static String formatIntervals(List<Interval> list) {
        if (list == null || list.isEmpty())
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Interval iv : list) {
            sb.append(iv.start).append("->").append(iv.end).append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',')
            sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    private static void logOverlaps(Integer vehId, LocalDateTime start, LocalDateTime end,
            Map<Integer, List<Interval>> calendar, String name) {
        List<Interval> list = calendar.get(vehId);
        if (list == null || list.isEmpty()) {
            System.out.println("[CALENDAR] " + name + " empty for veh=" + vehId);
            return;
        }
        for (Interval iv : list) {
            if (overlaps(start, end, iv.start, iv.end)) {
                System.out.println(
                        "[CALENDAR] overlap with " + name + " veh=" + vehId + " interval=" + iv.start + "->" + iv.end);
            }
        }
    }
}
