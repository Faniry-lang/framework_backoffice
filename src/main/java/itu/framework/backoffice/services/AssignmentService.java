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
            if (
                    //!heureCandidate.isBefore(heurePremiere) &&
                    !heureCandidate.isAfter(limiteAttente)) {
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

        // 1. Filtrer les véhicules compatibles (nombre de places suffisant)
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

        // 2. Trouver la capacité minimale optimale (nombre de places le plus proche du nombre de passagers)
        int capaciteMin = Integer.MAX_VALUE;
        for (Vehicule v : vehiculesCompatibles) {
            if (v.getNbrPlace() < capaciteMin) {
                capaciteMin = v.getNbrPlace();
            }
        }

        // 3. Filtrer les véhicules avec la capacité optimale
        List<Vehicule> meilleurCapacite = new ArrayList<>();
        for (Vehicule v : vehiculesCompatibles) {
            if (v.getNbrPlace() == capaciteMin) {
                meilleurCapacite.add(v);
            }
        }

        // Si un seul véhicule, le retourner
        if (meilleurCapacite.size() == 1) {
            return meilleurCapacite.get(0);
        }

// décommenter pour sprint 6
        System.out.println("====DEBUT====");
        System.out.println("VEHICULE DISPO: "+meilleurCapacite);

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
            System.out.println("Choisi: "+meilleurCapacite.get(0).toString()+" over "+meilleurCapacite);
            System.out.println("====FIN====");
            return meilleurCapacite.get(0);
        }

        List<Vehicule> vehiculesDiesel = new ArrayList<>();
        for (Vehicule v : meilleurCapacite) {
            if ("D".equals(v.getTypeCarburant())) {
                vehiculesDiesel.add(v);
            }
        }

        // Si des véhicules diesel sont disponibles, utiliser uniquement ceux-ci
        List<Vehicule> vehiculesFinaux = vehiculesDiesel.isEmpty() ? meilleurCapacite : vehiculesDiesel;

        // 5. Choix aléatoire parmi les véhicules restants
        if (vehiculesFinaux.size() == 1)
            return vehiculesFinaux.get(0);
        else
            System.out.println("SIZE FINAL: "+vehiculesFinaux.size());
            return vehiculesFinaux.get(new Random().nextInt(vehiculesFinaux.size()));
    }

    public void groupVehicles(List<TrajetCandidat> trajetCandidats) {
        if (trajetCandidats == null || trajetCandidats.isEmpty()) {
            //System.out.println("groupVehicles: aucun candidat fourni ou liste vide");
            return;
        }

        //System.out.println("groupVehicles: appelé avec " + trajetCandidats.size() + " candidats");

        List<TrajetCandidat> list = new ArrayList<>();
        for (TrajetCandidat tc : trajetCandidats) {
            if (tc == null) {
                //System.out.println("groupVehicles: candidat null dans input, skip");
                continue;
            }
            if (tc.getHeureDepart() == null || tc.getHeureArrivee() == null) {
                //System.out.println("groupVehicles: candidat sans heure depart/arrivee, skip: " + tc);
                continue;
            }
            list.add(tc);
        }

        list.sort(Comparator.comparing(TrajetCandidat::getHeureDepart, Comparator.nullsLast(Comparator.naturalOrder())));

        Set<TrajetCandidat> processed = new HashSet<>();

        for (TrajetCandidat base : new ArrayList<>(list)) {
            if (base == null) continue;
            if (processed.contains(base)) {
                //System.out.println("groupVehicles: base deja traitée, skip: " + base);
                continue;
            }
            if (base.getHeureDepart() == null) {
                //System.out.println("groupVehicles: base sans heureDepart, marque processed: " + base);
                processed.add(base);
                continue;
            }

            Integer tempsAttente = base.getTempsAttenteRestant() != null ? base.getTempsAttenteRestant() : 0;
            //System.out.println("groupVehicles: base heureDepart=" + base.getHeureDepart() + " tempsAttenteRestant=" + tempsAttente + " vehicule=" + (base.getVehicule()!=null?base.getVehicule().getId():null));

            if (tempsAttente <= 0) {
                //System.out.println("groupVehicles: tempsAttente <= 0 pour base, on marque processed et continue");
                processed.add(base);
                continue;
            }

            LocalDateTime windowStart = base.getHeureDepart();
            LocalDateTime windowEnd = windowStart.plusMinutes(tempsAttente);
            //System.out.println("groupVehicles: fenêtre de rassemblement [" + windowStart + " - " + windowEnd + "]");

            List<TrajetCandidat> group = new ArrayList<>();

            for (TrajetCandidat other : list) {
                if (other == null) continue;
                if (other == base) continue;
                if (processed.contains(other)) continue;
                if (other.getHeureDepart() == null) continue;
                LocalDateTime od = other.getHeureDepart();
                boolean inWindow = (!od.isBefore(windowStart)) && (!od.isAfter(windowEnd));
                //System.out.println("groupVehicles: comparaison autre depart=" + od + " inWindow=" + inWindow + " (base=" + windowStart + ") vehicule=" + (other.getVehicule()!=null?other.getVehicule().getId():null));
                if (inWindow) {
                    group.add(other);
                }
            }

            //System.out.println("groupVehicles: pour base depart=" + base.getHeureDepart() + " trouve " + group.size() + " autres candidats");

            if (group.isEmpty()) {
                //System.out.println("groupVehicles: aucun candidat à grouper avec base, marquage processed");
                processed.add(base);
                continue;
            }

            group.add(base);

            Optional<LocalDateTime> latestOpt = group.stream().map(TrajetCandidat::getHeureDepart).filter(Objects::nonNull).max(Comparator.naturalOrder());
            if (!latestOpt.isPresent()) {
                //System.out.println("groupVehicles: latestOpt absent, marquage de tout le group comme processed");
                processed.addAll(group);
                continue;
            }

            LocalDateTime newDepart = latestOpt.get();
            //System.out.println("groupVehicles: nouvelle heure de depart après regroupement = " + newDepart + " pour group size=" + group.size());

            for (TrajetCandidat tc : group) {
                if (tc == null) continue;
                if (tc.getHeureDepart() == null) {
                    //System.out.println("groupVehicles: candidat sans heureDepart dans group, marque processed: " + tc);
                    processed.add(tc);
                    continue;
                }
                try {
                    //System.out.println("groupVehicles: recalcul timing pour vehicule=" + (tc.getVehicule()!=null?tc.getVehicule().getId():null) + " avec depart=" + newDepart + " reservations=" + (tc.getReservations()!=null?tc.getReservations().size():0));
                    TripTiming recal = calculateTripTiming(tc.getVehicule(), newDepart, tc.getReservations(), tc.getOrdreVisites());
                    if (recal == null) {
                        //System.out.println("groupVehicles: recalcul a retourné null pour tc=" + tc + "; marquage processed");
                        processed.add(tc);
                        continue;
                    }
                    tc.setHeureDepart(recal.getHeureDepart());
                    tc.setHeureArrivee(recal.getHeureArrivee());
                    tc.setDistanceTotale(recal.getDistanceTotale());
                    //System.out.println("groupVehicles: recalcul OK pour vehicule=" + (tc.getVehicule()!=null?tc.getVehicule().getId():null) + " newDepart=" + recal.getHeureDepart() + " newArrivee=" + recal.getHeureArrivee() + " distance=" + recal.getDistanceTotale());
                    processed.add(tc);
                } catch (Exception e) {
                    //System.out.println("groupVehicles: exception lors du recalcul timing pour tc=" + tc + " error=" + e.getMessage());
                    processed.add(tc);
                }
            }
        }

        //System.out.println("groupVehicles: processing terminé, processed count=" + list.stream().filter(processed::contains).count());
        //System.out.println("groupVehicles: fin de la fonction");
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
