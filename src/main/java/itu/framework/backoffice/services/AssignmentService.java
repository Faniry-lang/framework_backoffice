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
    public AssignmentResult assignVehicles(LocalDate date) throws Exception {
        List<Reservation> reservationsDisponibles = Reservation.findUnassignedByDate(date);
        reservationsDisponibles.sort(Comparator.comparing(Reservation::getDateHeureArrivee));

        List<Vehicule> vehiculesDisponibles = new ArrayList<>(Vehicule.findAll(Vehicule.class));

        Lieux aeroport = Lieux.findAeroport();
        if (aeroport == null) {
            throw new Exception("Aéroport non trouvé dans la base de données");
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

            if (disponiblesPourTraitement.isEmpty()) {
                break;
            }

            Reservation premiereReservation = disponiblesPourTraitement.get(0);

            Vehicule meilleurVehicule = findBestVehicle(premiereReservation, vehiculesDisponibles);

            if (meilleurVehicule == null) {
                reservationsAssignees.add(premiereReservation.getId());
                continue;
            }

            List<Reservation> groupe = groupReservations(meilleurVehicule, date, disponiblesPourTraitement);

            if (!groupe.isEmpty()) {
                TrajetCandidat candidat = optimizeRoute(meilleurVehicule, groupe, aeroport);

                TripTiming timing = calculateTripTiming(meilleurVehicule, groupe, candidat.getOrdreVisites());
                if (timing != null) {
                    candidat.setHeureDepart(timing.getHeureDepart());
                    candidat.setHeureArrivee(timing.getHeureArrivee());
                    candidat.setDistanceTotale(timing.getDistanceTotale());
                }

                candidats.add(candidat);

                for (Reservation r : groupe) {
                    reservationsAssignees.add(r.getId());
                }

                vehiculesDisponibles.remove(meilleurVehicule);
            }

            if (vehiculesDisponibles.isEmpty()) {
                break;
            }
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
            if (!reservationsSauvegardees.contains(r.getId())) {
                reservationsNonAssignees.add(r);
            }
        }

        return new AssignmentResult(trajetsCreated, reservationsNonAssignees);
    }

    private List<Reservation> groupReservations(Vehicule vehicule, LocalDate date, List<Reservation> disponibles)
            throws Exception {
        List<Reservation> groupe = new ArrayList<>();
        if (disponibles.isEmpty()) {
            return groupe;
        }

        Reservation premiere = disponibles.get(0);
        Integer capaciteTotale = premiere.getNbPassager();

        if (capaciteTotale > vehicule.getNbrPlace()) {
            return groupe;
        }

        groupe.add(premiere);

        List<Trajet> trajets = vehicule.findTrajets(date);

        for (int i = 1; i < disponibles.size(); i++) {
            Reservation candidate = disponibles.get(i);
            int nouvelleCapacite = capaciteTotale + candidate.getNbPassager();
            if (nouvelleCapacite <= vehicule.getNbrPlace()
                    // decommenter pour sprint futur lorsque TEMPS ATTENTE pris en compte
                    // && candidate.getDateHeureArrivee().isBefore(
                    // premiere.getDateHeureArrivee().plusMinutes(premiere.getTempsAttenteMax())
                    // )
                    // decommenter pour sprint 4
                    && candidate.getDateHeureArrivee().equals(
                    premiere.getDateHeureArrivee()
                    )
                    && !vehicule.estOccupe(trajets,
                            premiere.getDateHeureArrivee().plusMinutes(premiere.getTempsAttenteMax()))) {
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
        if (groupe == null || groupe.isEmpty() || ordre == null || ordre.size() < 2) {
            return null;
        }
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
        if (reservation == null || disponibles == null || disponibles.isEmpty()) {
            return null;
        }

        Integer nbPassagers = reservation.getNbPassager();
        List<Vehicule> vehiculesCompatibles = new ArrayList<>();
        for (Vehicule v : disponibles) {
            if (v.getNbrPlace() >= nbPassagers) {
                vehiculesCompatibles.add(v);
            }
        }

        if (vehiculesCompatibles.isEmpty()) {
            return null;
        }

        int capaciteMin = Integer.MAX_VALUE;
        for (Vehicule v : vehiculesCompatibles) {
            if (v.getNbrPlace() < capaciteMin) {
                capaciteMin = v.getNbrPlace();
            }
        }

        List<Vehicule> meilleurCapacite = new ArrayList<>();
        for (Vehicule v : vehiculesCompatibles) {
            if (v.getNbrPlace() == capaciteMin) {
                meilleurCapacite.add(v);
            }
        }
        if (meilleurCapacite.size() == 1) {
            return meilleurCapacite.get(0);
        }
        Vehicule meilleur = meilleurCapacite.get(0);
        int meilleurePriorite = getFuelPriority(meilleur.getTypeCarburant());

        for (int i = 1; i < meilleurCapacite.size(); i++) {
            Vehicule candidat = meilleurCapacite.get(i);
            int prioriteCandidat = getFuelPriority(candidat.getTypeCarburant());

            if (prioriteCandidat < meilleurePriorite) {
                meilleur = candidat;
                meilleurePriorite = prioriteCandidat;
            }
        }

        return meilleur;
    }

    private int getFuelPriority(String typeCarburant) {
        String[] priorities = AssignmentConstants.FUEL_PRIORITY;
        for (int i = 0; i < priorities.length; i++) {
            if (priorities[i].equals(typeCarburant)) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
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
