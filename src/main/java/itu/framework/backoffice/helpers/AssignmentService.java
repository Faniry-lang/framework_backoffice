package itu.framework.backoffice.helpers;

import itu.framework.backoffice.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class AssignmentService {

    public static AssignmentResult assignVehicles(LocalDate date) throws Exception {
        // 1. Récupérer réservations non assignées du jour, trier par dateHeureArrivee
        // ASC
        List<Reservation> reservationsNonAssignees = Reservation.findUnassignedByDate(date);
        reservationsNonAssignees.sort(Comparator.comparing(Reservation::getDateHeureArrivee));

        // 2. Récupérer tous véhicules, trier par priorité de carburant
        List<Vehicule> vehicules = Vehicule.findAll(Vehicule.class);
        vehicules.sort(Comparator.comparing(Vehicule::getPriorite));

        List<Trajet> trajetsCreated = new ArrayList<>();
        List<Reservation> reservationsRestantes = new ArrayList<>(reservationsNonAssignees);

        // 3. Pour chaque véhicule, essayer de créer un trajet
        for (Vehicule vehicule : vehicules) {
            if (reservationsRestantes.isEmpty())
                break;

            // Vérifier si le véhicule est déjà assigné pour cette date
            if (Trajet.findByVehiculeAndDate(vehicule.getId(), date) != null) {
                continue; // Véhicule déjà occupé
            }

            // Grouper les réservations compatibles pour ce véhicule
            List<Reservation> reservationsGroupees = grouperReservationsCompatibles(
                    reservationsRestantes, vehicule, date);

            if (!reservationsGroupees.isEmpty()) {
                // Créer le trajet
                Trajet trajet = creerTrajet(vehicule, reservationsGroupees, date);
                if (trajet != null) {
                    trajetsCreated.add(trajet);
                    reservationsRestantes.removeAll(reservationsGroupees);
                }
            }
        }

        return new AssignmentResult(trajetsCreated, reservationsRestantes);
    }

    /**
     * Groupe les réservations compatibles pour un véhicule donné
     */
    private static List<Reservation> grouperReservationsCompatibles(
            List<Reservation> reservations, Vehicule vehicule, LocalDate date) throws Exception {

        List<Reservation> groupe = new ArrayList<>();
        int passagersTotal = 0;
        LocalDateTime derniereArrivee = null;

        Integer maxPassagers = Constants.Config.getMaxPassengersPerVehicle();

        for (Reservation reservation : reservations) {
            // Vérifier la capacité
            int nouveauTotal = passagersTotal + reservation.getNbPassager();
            if (nouveauTotal > vehicule.getNbrPlace() || nouveauTotal > maxPassagers) {
                continue; // Dépassement de capacité
            }

            // Vérifier la compatibilité temporelle
            if (derniereArrivee != null) {
                long minutesDifference = java.time.Duration.between(
                        derniereArrivee, reservation.getDateHeureArrivee()).toMinutes();

                // Si trop d'écart, ne pas grouper
                if (Math.abs(minutesDifference) > reservation.getTempsAttenteMaxEffectif()) {
                    continue;
                }
            }

            // Ajouter la réservation au groupe
            groupe.add(reservation);
            passagersTotal = nouveauTotal;

            if (derniereArrivee == null || reservation.getDateHeureArrivee().isAfter(derniereArrivee)) {
                derniereArrivee = reservation.getDateHeureArrivee();
            }

            // Si on atteint la capacité max, arrêter
            if (passagersTotal >= vehicule.getNbrPlace() || passagersTotal >= maxPassagers) {
                break;
            }
        }

        return groupe;
    }

    /**
     * Crée un trajet pour un véhicule et des réservations groupées
     */
    private static Trajet creerTrajet(Vehicule vehicule, List<Reservation> reservations, LocalDate date)
            throws Exception {
        if (reservations.isEmpty())
            return null;

        // Créer le trajet
        Trajet trajet = new Trajet();
        trajet.setIdVehicule(vehicule.getId());
        trajet.setDateTrajet(date);

        // Calculer l'ordre optimal des visites
        String ordreVisites = calculerOrdreOptimal(reservations);
        trajet.setOrdreVisites(ordreVisites);

        // Calculer les horaires et distance
        calculerHorairesEtDistance(trajet, vehicule, reservations);

        // Sauvegarder le trajet
        trajet.save();

        // Créer les liens trajet-réservation
        for (int i = 0; i < reservations.size(); i++) {
            TrajetReservation lien = new TrajetReservation();
            lien.setIdTrajet(trajet.getId());
            lien.setIdReservation(reservations.get(i).getId());
            lien.setOrdreVisite(i + 1);
            lien.save();
        }

        return trajet;
    }

    /**
     * Calcule l'ordre optimal des visites (algorithme simple)
     */
    private static String calculerOrdreOptimal(List<Reservation> reservations) throws Exception {
        List<String> codes = new ArrayList<>();

        // Commencer par l'aéroport
        codes.add("AERO01");

        // Ajouter les hôtels des réservations
        for (Reservation reservation : reservations) {
            Lieux hotel = (Lieux) reservation.getForeignKey("id_hotel");
            if (hotel != null && hotel.getCode() != null && !hotel.getAeroport()) {
                codes.add(hotel.getCode());
            }
        }

        // Retourner à l'aéroport
        codes.add("AERO01");

        return String.join(",", codes);
    }

    /**
     * Calcule les horaires et distance totale du trajet
     */
    private static void calculerHorairesEtDistance(Trajet trajet, Vehicule vehicule, List<Reservation> reservations)
            throws Exception {
        String[] etapes = trajet.getOrdreVisites().split(",");
        if (etapes.length < 2)
            return;

        // Prendre l'heure d'arrivée la plus tardive comme référence
        LocalDateTime heureReference = reservations.stream()
                .map(Reservation::getDateHeureArrivee)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        // Calculer le temps total nécessaire
        int tempsTotal = 0;
        double distanceTotale = 0;

        for (int i = 0; i < etapes.length - 1; i++) {
            String codeFrom = etapes[i].trim();
            String codeTo = etapes[i + 1].trim();

            // Distance
            Distance distanceObj = Distance.getDistance(codeFrom, codeTo);
            Double distance = distanceObj != null ? distanceObj.getDistanceKm() : 20.0;
            distanceTotale += distance;

            // Temps de trajet
            Integer tempsTrajet = vehicule.calculateTravelTime(codeFrom, codeTo);
            if (tempsTrajet != null) {
                tempsTotal += tempsTrajet;
            }

            // Temps d'arrêt
            if (i < etapes.length - 2) { // Pas d'arrêt à la dernière étape
                boolean isAeroport = "AERO01".equals(codeTo);
                int tempsArret = isAeroport ? Constants.Config.getAeroportStopTime()
                        : Constants.Config.getHotelStopTime();
                tempsTotal += tempsArret;
            }
        }

        // Définir les horaires (partir 30 minutes avant l'heure de référence)
        LocalDateTime heureDepart = heureReference.minusMinutes(tempsTotal + 30);
        LocalDateTime heureArrivee = heureDepart.plusMinutes(tempsTotal);

        trajet.setHeureDepart(heureDepart);
        trajet.setHeureArrivee(heureArrivee);
        trajet.setDistanceTotale(distanceTotale);
    }
}
