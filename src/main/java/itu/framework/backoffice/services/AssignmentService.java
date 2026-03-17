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
        if (intervals == null || intervals.isEmpty()) return date;
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

    public AssignmentResult assignVehicles(LocalDate date) throws Exception {
        List<Reservation> reservationsDisponibles = Reservation.findUnassignedByDate(date);
        reservationsDisponibles.sort(
                Comparator.comparingInt(AssignmentService::getPassengerPriorityScore).reversed()
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

        List<TrajetCandidat> candidats = new ArrayList<>();
        Set<Integer> reservationsAssignees = new HashSet<>();

        while (!reservationsDisponibles.isEmpty()) {
            List<Reservation> disponiblesPourTraitement = new ArrayList<>();
            for (Reservation r : reservationsDisponibles) {
                if (!reservationsAssignees.contains(r.getId())) {
                    disponiblesPourTraitement.add(r);
                }
            }

            if (disponiblesPourTraitement.isEmpty())
                break;

            Reservation premiereReservation = disponiblesPourTraitement.get(0);

            List<Vehicule> vehiculesCandidates = new ArrayList<>(vehiculesDisponibles);
            boolean assignedThisReservation = false;

            while (!vehiculesCandidates.isEmpty() && !assignedThisReservation) {
                Vehicule meilleurVehicule = findBestVehicle(premiereReservation, vehiculesCandidates);
                if (meilleurVehicule == null) {
                    reservationsAssignees.add(premiereReservation.getId());
                    break;
                }

                GroupeReservation groupeReservation = findOptimalWaitingGroup(meilleurVehicule, occupiedCalendar, disponiblesPourTraitement);
                List<Reservation> groupe = groupeReservation.getReservations();
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

                if (isVehicleFree(meilleurVehicule.getId(), start, end, occupiedCalendar)) {
                    candidats.add(candidat);
                    for (Reservation r : groupe)
                        reservationsAssignees.add(r.getId());
                    addIntervalToCalendar(meilleurVehicule.getId(), start, end, occupiedCalendar);
                    assignedThisReservation = true;
                } else {
                    vehiculesCandidates.remove(meilleurVehicule);
                }
            }

            if (!assignedThisReservation) {
                reservationsAssignees.add(premiereReservation.getId());
            }

            if (vehiculesDisponibles.isEmpty())
                break;
        }

        candidats.sort((c1, c2) -> Integer.compare(
                c2.getReservations().size(),
                c1.getReservations().size()));

        List<Trajet> trajetsCreated = new ArrayList<>();
        Set<Integer> reservationsSauvegardees = new HashSet<>();

        // a insérer juste avant ça
        //groupVehicles(candidats);

        for (TrajetCandidat candidat : candidats) {
            boolean toutesDisponibles = true;
            for (Reservation r : candidat.getReservations()) {
                if (reservationsSauvegardees.contains(r.getId())) {
                    toutesDisponibles = false;
                    break;
                }
            }

            if (toutesDisponibles && !candidat.getReservations().isEmpty()) {
                Trajet trajet = saveTrajet(candidat, date);
                trajetsCreated.add(trajet);
                for (Reservation r : candidat.getReservations()) {
                    reservationsSauvegardees.add(r.getId());
                }
            }
        }

        List<Reservation> reservationsNonAssignees = new ArrayList<>();
        for (Reservation r : reservationsDisponibles) {
            if (!reservationsSauvegardees.contains(r.getId()))
                reservationsNonAssignees.add(r);
        }

        return new AssignmentResult(trajetsCreated, reservationsNonAssignees);
    }

    private GroupeReservation findOptimalWaitingGroup(Vehicule vehicule, Map<Integer, List<Interval>> calendar, List<Reservation> disponibles)
            throws Exception {
        GroupeReservation groupeVide = new GroupeReservation();
        groupeVide.setReservations(new ArrayList<>());
        if (vehicule == null || disponibles == null || disponibles.isEmpty())
            return groupeVide;

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

        if (tempsAttenteRestant <= 0) {
            return groupeVide;
        }

        LocalDateTime limiteAttente = heurePremiere.plusMinutes(tempsAttenteRestant);

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
        if(reservationLaPlusTard.isEmpty()) throw new Exception("Erreur lors de l'obtention de la réservation la plus tardive");
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
        if(reservationLaPlusTard.isEmpty()) throw new Exception("Erreur lors de l'obtention de la réservation la plus tardive");
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

    private Vehicule findBestVehicle(Reservation reservation, List<Vehicule> disponibles) {
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

        List<Vehicule> vehiculesDiesel = new ArrayList<>();
        for (Vehicule v : meilleurCapacite)
            if ("D".equals(v.getTypeCarburant()))
                vehiculesDiesel.add(v);

        List<Vehicule> vehiculesFinaux;

        if(vehiculesDiesel.isEmpty()) {
            List<Vehicule> vehiculeEssence = new ArrayList<>();
            for(Vehicule v: meilleurCapacite) {
                if("ES".equals(v.getTypeCarburant())) {
                    vehiculeEssence.add(v);
                }
            }
            vehiculesFinaux = vehiculeEssence.isEmpty() ? meilleurCapacite : vehiculeEssence;
        } else {
            vehiculesFinaux = vehiculesDiesel;
        }

        if (vehiculesFinaux.size() == 1)
            return vehiculesFinaux.get(0);
        else
            return vehiculesFinaux.get(new Random().nextInt(vehiculesFinaux.size()));
    }

    public void groupVehicles(List<TrajetCandidat> trajetCandidats) {
        trajetCandidats.sort(Comparator.comparing(TrajetCandidat::getHeureDepart));
        List<TrajetCandidat> grouped = new ArrayList<>();
        for(TrajetCandidat t1: trajetCandidats) {
            Integer tempsAttenteRestant = t1.getTempsAttenteRestant();
            List<TrajetCandidat> groupForThisCandidate = new ArrayList<>();
            for(TrajetCandidat t2: trajetCandidats) {
                if(t2.equals(t1) || grouped.contains(t2)) continue;
                if(
                        isDateBetween(t1.getHeureDepart(), t1.getHeureDepart().plusMinutes(tempsAttenteRestant), t2.getHeureDepart())
                ) {
                    groupForThisCandidate.add(t2);
                }
            }
            LocalDateTime heureDepartLaPlusTardive =
                    groupForThisCandidate.stream().max(Comparator.comparing(TrajetCandidat::getHeureDepart)).get().getHeureDepart();
            for(TrajetCandidat tc : groupForThisCandidate) {
                tc.setHeureDepart(heureDepartLaPlusTardive);
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
