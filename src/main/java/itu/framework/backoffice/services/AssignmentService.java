package itu.framework.backoffice.services;

import itu.framework.backoffice.constantes.AssignmentConstants;
import itu.framework.backoffice.entities.*;
import itu.framework.backoffice.models.AssignmentResult;
import itu.framework.backoffice.models.TrajetCandidat;
import itu.framework.backoffice.models.TripTiming;

import java.math.BigDecimal;
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
        calendar.computeIfAbsent(vehiculeId, k -> new ArrayList<>()).add(new Interval(start, end));
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

                List<Reservation> groupe = findOptimalWaitingGroup(meilleurVehicule, disponiblesPourTraitement);
                if (groupe.isEmpty()) {
                    vehiculesCandidates.remove(meilleurVehicule);
                    continue;
                }

                TrajetCandidat candidat = optimizeRoute(meilleurVehicule, groupe, aeroport);
                TripTiming timing = calculateTripTiming(meilleurVehicule, groupe, candidat.getOrdreVisites());
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

            boolean anyVehicleLeft = false;
            for (Vehicule v : vehiculesDisponibles) {
                // boucle la vehicule raha miodina le boucle de mbola misy vehicule zany
                if (true) {
                    anyVehicleLeft = true;
                    break;
                }
            }
            if (!anyVehicleLeft)
                break;
        }

        candidats.sort((c1, c2) -> Integer.compare(
                c2.getReservations().size(),
                c1.getReservations().size()));

        List<Trajet> trajetsCreated = new ArrayList<>();
        Set<Integer> reservationsSauvegardees = new HashSet<>();

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

    private List<Reservation> findOptimalWaitingGroup(Vehicule vehicule, List<Reservation> disponibles)
            throws Exception {
        List<Reservation> groupeVide = new ArrayList<>();
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
            return groupe;
        }

        LocalDateTime limiteAttente = heurePremiere.plusMinutes(premiere.getTempsAttenteMaxEffectif());

        TreeSet<LocalDateTime> pointsDepart = new TreeSet<>();
        pointsDepart.add(heurePremiere); // partir immédiatement
        for (int i = 1; i < disponibles.size(); i++) {
            Reservation candidate = disponibles.get(i);
            if (candidate == null || candidate.getDateHeureArrivee() == null)
                continue;
            LocalDateTime heureCandidate = candidate.getDateHeureArrivee();
            // Fenêtre glissante : on teste aussi l'option d'attendre les prochains vols
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
                // A score égal, on évite d'attendre inutilement
                meilleur = true;
            }

            if (meilleur) {
                meilleurNbPassagers = nbPassagers;
                meilleurNbReservations = groupeCandidat.size();
                meilleurDepart = departCandidat;
                meilleurGroupe = groupeCandidat;
            }
        }

        return meilleurGroupe;
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

    private TrajetCandidat optimizeRoute(Vehicule vehicule, List<Reservation> groupe, Lieux aeroport) throws Exception {
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

        LocalDateTime heureDepart = groupe.get(0).getDateHeureArrivee();
        double minutesTrajet = distanceTotal.doubleValue() / vehicule.getVitesseMoyenne() * 60;
        LocalDateTime heureArrivee = heureDepart.plusMinutes((long) minutesTrajet);

        return new TrajetCandidat(vehicule, groupe, heureDepart, heureArrivee, distanceTotal, ordreVisites);
    }

    private TripTiming calculateTripTiming(Vehicule v, List<Reservation> groupe, List<String> ordre) throws Exception {
        if (groupe == null || groupe.isEmpty() || ordre == null || ordre.size() < 2)
            return null;
        LocalDateTime heureDepart = groupe.get(0).getDateHeureArrivee();

        BigDecimal distanceTotale = BigDecimal.ZERO;
        long dureeMinutesTotale = 0;
        for (int i = 0; i < ordre.size() - 1; i++) {
            String codeFrom = ordre.get(i);
            String codeTo = ordre.get(i + 1);

            Distance distance = Distance.getDistance(codeFrom, codeTo);
            if (distance != null) {
                BigDecimal distanceKm = distance.getDistanceKm();
                distanceTotale = distanceTotale.add(distanceKm);
                double dureeMinutes = distanceKm.doubleValue() / v.getVitesseMoyenne() * 60;
                dureeMinutesTotale += (long) dureeMinutes;
            }
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

        List<Vehicule> vehiculesFinaux = vehiculesDiesel.isEmpty() ? meilleurCapacite : vehiculesDiesel;

        if (vehiculesFinaux.size() == 1)
            return vehiculesFinaux.get(0);
        else
            return vehiculesFinaux.get(new Random().nextInt(vehiculesFinaux.size()));
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
