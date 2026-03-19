package itu.framework.backoffice.services;

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
        if (start == null || end == null || date == null) return false;
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

    private static int getPassengerPriorityScore(Reservation reservation) {
        Integer nbPassager = reservation.getNbPassager();
        if (nbPassager == null)
            return 0;
        if (nbPassager >= 5)
            return 3;
        if (nbPassager >= 3)
            return 2;
        return 1;
    }

    private static LocalDateTime getClosestAvailableDateForVehicle(Integer vehicleId, LocalDateTime date, Map<Integer, List<Interval>> calendar) {
        if (vehicleId == null || date == null) return date;
        List<Interval> intervals = calendar.get(vehicleId);
        System.out.println("[CALENDAR] getClosestAvailableDateForVehicle vehicle=" + vehicleId + " requested=" + date + " intervals=" + formatIntervals(intervals));
        if (intervals == null || intervals.isEmpty()) {
            System.out.println("[CALENDAR] no intervals for vehicle=" + vehicleId + " -> return requested date");
            return date;
        }
        intervals.sort(Comparator.comparing(i -> i.start));
        for (Interval iv : intervals) {
            if (isDateBetween(iv.start, iv.end, date) || iv.start.isEqual(date)) {
                System.out.println("[CALENDAR] requested inside interval " + iv.start + "->" + iv.end + " -> return " + iv.end);
                return iv.end;
            }
            if (date.isBefore(iv.start)) {
                System.out.println("[CALENDAR] requested before next interval " + iv.start + " -> return requested date");
                return date;
            }
        }
        System.out.println("[CALENDAR] requested after all intervals -> return requested date");
        return date;
    }

    private static Map<Integer, List<Interval>> copyCalendar(Map<Integer, List<Interval>> src) {
        Map<Integer, List<Interval>> copy = new HashMap<>();
        if (src == null) return copy;
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
        if (vehiculeId == null) return false;
        if (!isVehicleFree(vehiculeId, start, end, occupiedCalendar)) return false;
        if (!isVehicleFree(vehiculeId, start, end, tentativeCalendar)) return false;
        return true;
    }

    private static String formatIntervals(List<Interval> list) {
        if (list == null || list.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Interval iv : list) {
            sb.append(iv.start).append("->").append(iv.end).append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',') sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    private static void logOverlaps(Integer vehId, LocalDateTime start, LocalDateTime end, Map<Integer, List<Interval>> calendar, String name) {
        List<Interval> list = calendar.get(vehId);
        if (list == null || list.isEmpty()) {
            System.out.println("[CALENDAR] " + name + " empty for veh=" + vehId);
            return;
        }
        for (Interval iv : list) {
            if (overlaps(start, end, iv.start, iv.end)) {
                System.out.println("[CALENDAR] overlap with " + name + " veh=" + vehId + " interval=" + iv.start + "->" + iv.end);
            }
        }
    }

    public AssignmentResult assignVehicles(LocalDate date) throws Exception {
        System.out.println("[ASSIGN] start assignVehicles for date=" + date);
        List<Reservation> reservationsDisponibles = Reservation.findUnassignedByDate(date);
        reservationsDisponibles.sort(
                Comparator.comparingInt(AssignmentService::getPassengerPriorityScore).reversed()
                        .thenComparing(Reservation::getNbPassager, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Reservation::getDateHeureArrivee,
                                Comparator.nullsLast(Comparator.naturalOrder())));

        System.out.println("[ASSIGN] reservations count=" + reservationsDisponibles.size());
        for (Reservation r : reservationsDisponibles) {
            System.out.println("[ASSIGN] res id=" + r.getId() + " client=" + r.getIdClient() + " nb=" + r.getNbPassager() + " arrivee=" + r.getDateHeureArrivee());
        }

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
        List<Reservation> remainingReservations = new ArrayList<>(reservationsDisponibles);

        while (!remainingReservations.isEmpty()) {
            System.out.println("[ASSIGN] remainingReservations=" + remainingReservations.stream().map(r -> r.getId() + ":" + r.getIdClient()).collect(Collectors.joining(",")));
            List<Reservation> disponiblesPourTraitement = new ArrayList<>(remainingReservations);

            if (disponiblesPourTraitement.isEmpty())
                break;

            Reservation premiereReservation = disponiblesPourTraitement.get(0);
            System.out.println("[ASSIGN] premiereReservation id=" + (premiereReservation != null ? premiereReservation.getId() : null) + " client=" + (premiereReservation != null ? premiereReservation.getIdClient() : null) + " arrivee=" + (premiereReservation != null ? premiereReservation.getDateHeureArrivee() : null));

            List<Vehicule> vehiculesCandidates = new ArrayList<>(vehiculesDisponibles);
            boolean assignedThisReservation = false;

            while (!vehiculesCandidates.isEmpty() && !assignedThisReservation) {
                System.out.println("[ASSIGN] vehiculesCandidates size=" + vehiculesCandidates.size());
                Vehicule meilleurVehicule = findBestVehicle(premiereReservation, candidats, vehiculesCandidates);
                System.out.println("[ASSIGN] findBestVehicle returned=" + (meilleurVehicule != null ? meilleurVehicule.getRef() + "(id=" + meilleurVehicule.getId() + ")" : "null"));
                if (meilleurVehicule == null) {
                    reservationsAssignees.add(premiereReservation.getId());
                    System.out.println("[ASSIGN] marque comme processed (no vehicle) id=" + premiereReservation.getId() + " client=" + premiereReservation.getIdClient());
                    break;
                }

                GroupeReservation groupeReservation = findOptimalWaitingGroup(meilleurVehicule, tentativeCalendar, disponiblesPourTraitement);
                List<Reservation> groupe = groupeReservation.getReservations();
                System.out.println("[ASSIGN] groupeReservation size=" + (groupe != null ? groupe.size() : 0));
                if (groupe != null && !groupe.isEmpty()) {
                    String ids = groupe.stream().map(r -> String.valueOf(r.getId())).collect(Collectors.joining(","));
                    System.out.println("[ASSIGN] groupe reservations ids=[" + ids + "]");
                }
                if (groupe.isEmpty()) {
                    vehiculesCandidates.remove(meilleurVehicule);
                    continue;
                }

                TrajetCandidat candidat = optimizeRoute(meilleurVehicule, groupeReservation.getHeureArriveeVehicule(), groupe, aeroport);
                TripTiming timing = calculateTripTiming(meilleurVehicule, groupeReservation.getHeureArriveeVehicule(), groupe, candidat.getOrdreVisites());
                if (timing != null) {
                    candidat.setHeureDepart(timing.getHeureDepart());
                    candidat.setHeureArrivee(timing.getHeureArrivee());
                    candidat.setDistanceTotale(timing.getDistanceTotale());
                }

                LocalDateTime start = candidat.getHeureDepart();
                LocalDateTime end = candidat.getHeureArrivee();

                System.out.println("[ASSIGN] candidat vehicule=" + candidat.getVehicule().getRef() + " start=" + start + " end=" + end + " distance=" + candidat.getDistanceTotale());

                System.out.println("[CALENDAR] occupied for veh=" + meilleurVehicule.getId() + " = " + formatIntervals(occupiedCalendar.get(meilleurVehicule.getId())));
                System.out.println("[CALENDAR] tentative for veh=" + meilleurVehicule.getId() + " = " + formatIntervals(tentativeCalendar.get(meilleurVehicule.getId())));

                if (isVehicleFreeOverall(meilleurVehicule.getId(), start, end, occupiedCalendar, tentativeCalendar)) {
                    System.out.println("[ASSIGN] vehicule free overall id=" + meilleurVehicule.getId());
                    candidats.add(candidat);
                    for (Reservation r : groupe) {
                        reservationsAssignees.add(r.getId());
                        System.out.println("[ASSIGN] marque comme assignee id=" + r.getId() + " client=" + r.getIdClient());
                    }
                    addIntervalToCalendar(meilleurVehicule.getId(), start, end, tentativeCalendar);
                    assignedThisReservation = true;
                } else {
                    System.out.println("[ASSIGN] vehicule NOT free overall id=" + meilleurVehicule.getId() + " start=" + start + " end=" + end);
                    logOverlaps(meilleurVehicule.getId(), start, end, occupiedCalendar, "occupiedCalendar");
                    logOverlaps(meilleurVehicule.getId(), start, end, tentativeCalendar, "tentativeCalendar");
                    vehiculesCandidates.remove(meilleurVehicule);
                }
            }

            if (!assignedThisReservation) {
                reservationsAssignees.add(premiereReservation.getId());
                System.out.println("[ASSIGN] marque comme processed (exhausted vehicles) id=" + (premiereReservation != null ? premiereReservation.getId() : null) + " client=" + (premiereReservation != null ? premiereReservation.getIdClient() : null));
            }

            if (!candidats.isEmpty()) {
                try {
                    System.out.println("[ASSIGN] avant groupVehicles candidats count=" + candidats.size());
                    groupVehicles(candidats);
                    System.out.println("[ASSIGN] apres groupVehicles candidats count=" + candidats.size());
                } catch (Exception e) {
                    System.out.println("[WARN] groupVehicles a échoué: " + e.getMessage());
                }

                tentativeCalendar.clear();
                for (TrajetCandidat c : candidats) {
                    if (c == null || c.getVehicule() == null || c.getHeureDepart() == null || c.getHeureArrivee() == null)
                        continue;
                    addIntervalToCalendar(c.getVehicule().getId(), c.getHeureDepart(), c.getHeureArrivee(), tentativeCalendar);
                }

                Iterator<TrajetCandidat> it = candidats.iterator();
                while (it.hasNext()) {
                    TrajetCandidat c = it.next();
                    if (c == null) {
                        it.remove();
                        continue;
                    }
                    Vehicule veh = c.getVehicule();
                    List<Reservation> grp = c.getReservations();
                    List<String> ordre = c.getOrdreVisites();
                    LocalDateTime heurDepartCandidate = c.getHeureDepart();
                    try {
                        TripTiming nt = calculateTripTiming(veh, heurDepartCandidate, grp, ordre);
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
                    if (c == null || c.getVehicule() == null || c.getHeureDepart() == null || c.getHeureArrivee() == null)
                        continue;
                    addIntervalToCalendar(c.getVehicule().getId(), c.getHeureDepart(), c.getHeureArrivee(), tentativeCalendar);
                }
            }

            if (vehiculesDisponibles.isEmpty())
                break;

            Iterator<Reservation> remIt = remainingReservations.iterator();
            while (remIt.hasNext()) {
                Reservation r = remIt.next();
                if (reservationsAssignees.contains(r.getId())) {
                    remIt.remove();
                }
            }
        }

        candidats.sort((c1, c2) -> Integer.compare(
                c2.getReservations().size(),
                c1.getReservations().size()));

        List<Trajet> trajetsCreated = new ArrayList<>();
        Set<Integer> reservationsSauvegardees = new HashSet<>();

        Map<Integer, List<Interval>> occupiedToSave = copyCalendar(occupiedCalendar);
        for (TrajetCandidat candidat : candidats) {
            if (candidat == null || candidat.getVehicule() == null || candidat.getHeureDepart() == null || candidat.getHeureArrivee() == null)
                continue;
            Integer vid = candidat.getVehicule().getId();
            LocalDateTime start = candidat.getHeureDepart();
            LocalDateTime end = candidat.getHeureArrivee();

            if (!isVehicleFree(vid, start, end, occupiedToSave)) {
                System.out.println("[SAVE] skip candidat veh=" + vid + " start=" + start + " end=" + end + " cause overlap with occupiedToSave=" + formatIntervals(occupiedToSave.get(vid)));
                continue;
            }

            boolean toutesDisponibles = true;
            for (Reservation r : candidat.getReservations()) {
                if (reservationsSauvegardees.contains(r.getId())) {
                    toutesDisponibles = false;
                    break;
                }
            }

            if (!toutesDisponibles || candidat.getReservations().isEmpty()) {
                System.out.println("[SAVE] skip candidat because reservations already saved or empty, ids=" + (candidat.getReservations() == null ? "[]" : candidat.getReservations().stream().map(res -> String.valueOf(res.getId())).collect(Collectors.joining(","))));
                continue;
            }

            Trajet trajet = saveTrajet(candidat, date);
            trajetsCreated.add(trajet);
            for (Reservation r : candidat.getReservations()) {
                reservationsSauvegardees.add(r.getId());
            }
            addIntervalToCalendar(vid, start, end, occupiedToSave);
            System.out.println("[SAVE] saved trajet id=" + trajet.getId() + " veh=" + vid + " interval=" + start + "->" + end);
        }

        List<Reservation> reservationsNonAssignees = new ArrayList<>(remainingReservations);

        System.out.println("[ASSIGN] finished assignVehicles, trajetsCreated=" + trajetsCreated.size() + " nonAssigned=" + reservationsNonAssignees.size());

        return new AssignmentResult(trajetsCreated, reservationsNonAssignees);
    }

    private GroupeReservation findOptimalWaitingGroup(Vehicule vehicule, Map<Integer, List<Interval>> calendar, List<Reservation> disponibles)
            throws Exception {
        GroupeReservation groupeVide = new GroupeReservation();
        groupeVide.setReservations(new ArrayList<>());
        if (vehicule == null || disponibles == null || disponibles.isEmpty())
            return groupeVide;
        System.out.print("[GROUP] Reservation disponibles: ");
        for(Reservation r : disponibles) {
            System.out.print(r.getIdClient()+" ");
        }
        System.out.println("");

        Reservation premiere = disponibles.get(0);
        if (premiere == null)
            return groupeVide;

        Integer nbPremiere = premiere.getNbPassager();
        if (nbPremiere == null || nbPremiere > vehicule.getNbrPlace())
            return groupeVide;

        LocalDateTime heurePremiere = premiere.getDateHeureArrivee();
        if (heurePremiere == null) {
            List<Reservation> groupe = new ArrayList<>();
            groupe.add(premiere);
            groupeVide.setReservations(groupe);
            return groupeVide;
        }

        LocalDateTime heureArrivee = getClosestAvailableDateForVehicle(vehicule.getId(), heurePremiere, calendar);

        long attenteDeja = Math.max(0, Duration.between(heurePremiere, heureArrivee).toMinutes());
        Integer tempsAttenteMaxEffectif = premiere.getTempsAttenteMaxEffectif() == null ? 0 : premiere.getTempsAttenteMaxEffectif();
        Integer tempsAttenteRestant = tempsAttenteMaxEffectif - (int) attenteDeja;

        System.out.println("[GROUP] findOptimalWaitingGroup vehicule=" + vehicule.getRef() + " premiere=" + premiere.getId() + " heurePremiere=" + heurePremiere + " heureArriveeVehicule=" + heureArrivee + " tempsAttenteRestant=" + tempsAttenteRestant);

        if (tempsAttenteRestant <= 0) {
            return groupeVide;
        }

        LocalDateTime limiteAttente = heureArrivee.plusMinutes(tempsAttenteRestant);
        if(vehicule.getRef().equals("V-002")) {
            System.out.println("[DEBUG] temps attente restant: "+tempsAttenteRestant);
            System.out.println("limite attente: "+limiteAttente);
            System.out.println("disponibles: ");
            for(Reservation r:disponibles){
                System.out.println("      "+r.getIdClient());
            }
            System.out.println("[FIN DEBUG]\n");
        }

        TreeSet<LocalDateTime> pointsDepart = new TreeSet<>();
        pointsDepart.add(heurePremiere);
        for (int i = 1; i < disponibles.size(); i++) {
            Reservation candidate = disponibles.get(i);
            if (candidate == null || candidate.getDateHeureArrivee() == null)
                continue;
            LocalDateTime heureCandidate = candidate.getDateHeureArrivee();
            if (!heureCandidate.isBefore(heurePremiere) && !heureCandidate.isAfter(limiteAttente)) {
                pointsDepart.add(heureCandidate);
            }
        }

        System.out.println("[GROUP] pointsDepart=" + pointsDepart);

        List<Reservation> meilleurGroupe = new ArrayList<>();
        int meilleurNbPassagers = -1;
        int meilleurNbReservations = -1;
        LocalDateTime meilleurDepart = null;

        for (LocalDateTime departCandidat : pointsDepart) {
            List<Reservation> groupeCandidat = buildGroupAtDeparture(vehicule, premiere, disponibles, departCandidat);
            if (groupeCandidat.isEmpty())
                continue;

            int nbPassagers = 0;
            for (Reservation reservation : groupeCandidat) {
                if (reservation.getNbPassager() != null) {
                    nbPassagers += reservation.getNbPassager();
                }
            }

            String ids = groupeCandidat.stream().map(r -> String.valueOf(r.getId())).collect(Collectors.joining(","));
            System.out.println("[GROUP] departCandidat=" + departCandidat + " groupeSize=" + groupeCandidat.size() + " ids=[" + ids + "] nbPassagers=" + nbPassagers);

            boolean meilleur = false;
            if (nbPassagers > meilleurNbPassagers) {
                meilleur = true;
            } else if (nbPassagers == meilleurNbPassagers
                    && groupeCandidat.size() > meilleurNbReservations) {
                meilleur = true;
            } else if (nbPassagers == meilleurNbPassagers
                    && groupeCandidat.size() == meilleurNbReservations
                    && (meilleurDepart == null || departCandidat.isBefore(meilleurDepart))) {
                meilleur = true;
            }

            if (meilleur) {
                meilleurNbPassagers = nbPassagers;
                meilleurNbReservations = groupeCandidat.size();
                meilleurDepart = departCandidat;
                meilleurGroupe = groupeCandidat;
                System.out.println("[GROUP] meilleur updated depart=" + meilleurDepart + " nbPassagers=" + meilleurNbPassagers + " nbReservations=" + meilleurNbReservations);
            }
        }

        GroupeReservation gp = new GroupeReservation();
        gp.setReservations(meilleurGroupe);
        gp.setHeureArriveeVehicule(heureArrivee);
        return gp;
    }

    private List<Reservation> buildGroupAtDeparture(Vehicule vehicule, Reservation premiere,
                                                    List<Reservation> disponibles, LocalDateTime departCandidat) {
        List<Reservation> groupe = new ArrayList<>();
        int capaciteTotale = 0;

        if (premiere.getNbPassager() == null)
            return groupe;

        groupe.add(premiere);
        capaciteTotale = premiere.getNbPassager();

        if (capaciteTotale > vehicule.getNbrPlace())
            return new ArrayList<>();

        List<Reservation> candidates = new ArrayList<>();
        for (int i = 1; i < disponibles.size(); i++) {
            Reservation candidate = disponibles.get(i);
            if (candidate == null || candidate.getDateHeureArrivee() == null)
                continue;

            LocalDateTime heureCandidate = candidate.getDateHeureArrivee();
            LocalDateTime heurePremiere = premiere.getDateHeureArrivee();

            if (!heureCandidate.isBefore(heurePremiere) && !heureCandidate.isAfter(departCandidat)) {
                candidates.add(candidate);
            }
        }

        candidates.sort(Comparator
                .comparing(Reservation::getDateHeureArrivee)
                .thenComparing(Reservation::getNbPassager, Comparator.nullsLast(Comparator.reverseOrder())));

        for (Reservation candidate : candidates) {
            if (candidate.getNbPassager() == null)
                continue;
            int nouvelleCapacite = capaciteTotale + candidate.getNbPassager();
            if (nouvelleCapacite <= vehicule.getNbrPlace()) {
                groupe.add(candidate);
                capaciteTotale = nouvelleCapacite;
            }
        }

        return groupe;
    }

    private TrajetCandidat optimizeRoute(Vehicule vehicule, LocalDateTime heureArriveeVehicule, List<Reservation> groupe, Lieux aeroport) throws Exception {
        List<String> ordreVisites = new ArrayList<>();
        ordreVisites.add(aeroport.getCode());

        BigDecimal distanceTotal = BigDecimal.ZERO;
        Lieux positionActuelle = aeroport;

        Set<Integer> nonVisites = new HashSet<>();
        Map<Integer, Lieux> hotelCache = new HashMap<>();

        for (Reservation reservation : groupe) {
            nonVisites.add(reservation.getId());
            Lieux hotel = reservation.getForeignKey("id_hotel");
            hotelCache.put(reservation.getId(), hotel);
        }

        while (!nonVisites.isEmpty()) {
            BigDecimal minDistance = BigDecimal.valueOf(Double.MAX_VALUE);
            Reservation plusProche = null;
            Lieux hotelPlusProche = null;

            for (Reservation reservation : groupe) {
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

        Optional<Reservation> reservationLaPlusTard = groupe.stream().max(Comparator.comparing(Reservation::getDateHeureArrivee));
        if (!reservationLaPlusTard.isPresent()) throw new Exception("Erreur lors de l'obtention de la réservation la plus tardive");
        LocalDateTime lastArrival = reservationLaPlusTard.get().getDateHeureArrivee();
        LocalDateTime heureDepart = heureArriveeVehicule != null ? heureArriveeVehicule : lastArrival;
        if (heureDepart.isBefore(lastArrival)) {
            heureDepart = lastArrival;
        }

        double vitesse = vehicule.getVitesseMoyenne() == null || vehicule.getVitesseMoyenne() <= 0.0 ? 60.0 : vehicule.getVitesseMoyenne();
        double minutesTrajet = distanceTotal.doubleValue() / vitesse * 60.0;
        long minutesArrondis = (long) Math.ceil(minutesTrajet);
        LocalDateTime heureArrivee = heureDepart.plusMinutes(minutesArrondis);

        return new TrajetCandidat(vehicule, groupe, heureDepart, heureArrivee, distanceTotal, ordreVisites);
    }

    private TripTiming calculateTripTiming(Vehicule v, LocalDateTime heureArriveeVehicule, List<Reservation> groupe, List<String> ordre) throws Exception {
        if (groupe == null || groupe.isEmpty() || ordre == null || ordre.size() < 2)
            return null;
        Optional<Reservation> reservationLaPlusTard = groupe.stream().max(Comparator.comparing(Reservation::getDateHeureArrivee));
        if (!reservationLaPlusTard.isPresent()) throw new Exception("Erreur lors de l'obtention de la réservation la plus tardive");
        LocalDateTime lastArrival = reservationLaPlusTard.get().getDateHeureArrivee();
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

    private Vehicule findBestVehicle(Reservation reservation, List<TrajetCandidat> candidats, List<Vehicule> disponibles) {
        if (reservation == null || disponibles == null || disponibles.isEmpty())
            return null;

        Integer nbPassagers = reservation.getNbPassager();
        List<Vehicule> vehiculesCompatibles = new ArrayList<>();
        for (Vehicule v : disponibles) {
            if (v.getNbrPlace() >= nbPassagers)
                vehiculesCompatibles.add(v);
        }

        if (vehiculesCompatibles.isEmpty())
            return null;

        int capaciteMin = Integer.MAX_VALUE;
        for (Vehicule v : vehiculesCompatibles)
            if (v.getNbrPlace() < capaciteMin)
                capaciteMin = v.getNbrPlace();

        List<Vehicule> meilleurCapacite = new ArrayList<>();
        for (Vehicule v : vehiculesCompatibles)
            if (v.getNbrPlace() == capaciteMin)
                meilleurCapacite.add(v);

        if (meilleurCapacite.size() == 1)
            return meilleurCapacite.get(0);

        System.out.println("====DEBUT findBestVehicle====");
        System.out.println("VEHICULE DISPO: " + meilleurCapacite);

        Map<Integer, Long> candidatsVehicules = new HashMap<>();

        for(Vehicule v : meilleurCapacite) {
            Long count = 0L;
            for(TrajetCandidat tc: candidats) {
                if(tc.getVehicule().getId().equals(v.getId())) {
                    count++;
                }
            }
            candidatsVehicules.put(v.getId(), count);
        }

        for(Map.Entry<Integer, Long> entry : candidatsVehicules.entrySet()) {
            System.out.println("VEHICULE: "+entry.getKey()+", CANDIDATS: "+entry.getValue());
        }

        Long nbrCandidats = candidatsVehicules.values()
                .stream()
                .min(Long::compareTo)
                .orElse(0L);

        Iterator<Vehicule> it = meilleurCapacite.iterator();
        System.out.println("MIN NBR CANDIDATS: "+nbrCandidats);

        List<Vehicule> nonConsideres = new ArrayList<>();

        while (it.hasNext()) {
            Vehicule v = it.next();
            Long count = candidatsVehicules.getOrDefault(v.getId(), 0L);
            System.out.println("VEHICULE: "+v.getId()+", COUNT: "+count+", CANDIDATS: "+nbrCandidats);
            if (count > nbrCandidats) {
                nonConsideres.add(v);
            }
        }

        for(Vehicule v : nonConsideres) {
            meilleurCapacite.remove(v);
        }

        if(meilleurCapacite.size() == 1) {
            System.out.println("Choisi: " + meilleurCapacite.get(0).toString() + " over " + meilleurCapacite);
            System.out.println("====FIN====");
            return meilleurCapacite.get(0);
        }

        List<Vehicule> vehiculesDiesel = new ArrayList<>();
        for (Vehicule v : meilleurCapacite)
            if ("D".equals(v.getTypeCarburant()))
                vehiculesDiesel.add(v);

        List<Vehicule> vehiculesFinaux;


        vehiculesFinaux = vehiculesDiesel;

        if (vehiculesFinaux.size() == 1) {
            return vehiculesFinaux.get(0);
        }
        else {
            System.out.println("SIZE FINAL: "+vehiculesFinaux.size());
            return vehiculesFinaux.get(new Random().nextInt(vehiculesFinaux.size()));
        }
    }

    public void groupVehicles(List<TrajetCandidat> trajetCandidats) {
        if (trajetCandidats == null || trajetCandidats.isEmpty()) {
            return;
        }


        List<TrajetCandidat> list = new ArrayList<>();
        for (TrajetCandidat tc : trajetCandidats) {
            if (tc == null) {
                continue;
            }
            if (tc.getHeureDepart() == null || tc.getHeureArrivee() == null) {
                continue;
            }
            list.add(tc);
        }

        list.sort(Comparator.comparing(TrajetCandidat::getHeureDepart, Comparator.nullsLast(Comparator.naturalOrder())));

        Set<TrajetCandidat> processed = new HashSet<>();

        for (TrajetCandidat base : new ArrayList<>(list)) {
            if (base == null) continue;
            if (processed.contains(base)) {
                continue;
            }
            if (base.getHeureDepart() == null) {
                processed.add(base);
                continue;
            }

            Integer tempsAttente = base.getTempsAttenteRestant() != null ? base.getTempsAttenteRestant() : 0;

            if (tempsAttente <= 0) {
                processed.add(base);
                continue;
            }

            LocalDateTime windowStart = base.getHeureDepart();
            LocalDateTime windowEnd = windowStart.plusMinutes(tempsAttente);

            List<TrajetCandidat> group = new ArrayList<>();

            for (TrajetCandidat other : list) {
                if (other == null) continue;
                if (other == base) continue;
                if (processed.contains(other)) continue;
                if (other.getHeureDepart() == null) continue;
                LocalDateTime od = other.getHeureDepart();
                boolean inWindow = (!od.isBefore(windowStart)) && (!od.isAfter(windowEnd));
                if (inWindow) {
                    group.add(other);
                }
            }


            if (group.isEmpty()) {
                processed.add(base);
                continue;
            }

            group.add(base);

            Optional<LocalDateTime> latestOpt = group.stream().map(TrajetCandidat::getHeureDepart).filter(Objects::nonNull).max(Comparator.naturalOrder());
            if (!latestOpt.isPresent()) {
                processed.addAll(group);
                continue;
            }

            LocalDateTime newDepart = latestOpt.get();

            for (TrajetCandidat tc : group) {
                if (tc == null) continue;
                if (tc.getHeureDepart() == null) {
                    processed.add(tc);
                    continue;
                }
                try {
                    TripTiming recal = calculateTripTiming(tc.getVehicule(), newDepart, tc.getReservations(), tc.getOrdreVisites());
                    if (recal == null) {
                        processed.add(tc);
                        continue;
                    }
                    tc.setHeureDepart(recal.getHeureDepart());
                    tc.setHeureArrivee(recal.getHeureArrivee());
                    tc.setDistanceTotale(recal.getDistanceTotale());
                    processed.add(tc);
                } catch (Exception e) {
                    processed.add(tc);
                }
            }
        }
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

        List<Reservation> reservations = candidat.getReservations();
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);

            TrajetReservation tr = new TrajetReservation();
            tr.setIdTrajet(trajet.getId().intValue());
            tr.setIdReservation(reservation.getId());
            tr.setOrdreVisite(i + 1);

            tr.save();
        }

        return trajet;
    }
}
