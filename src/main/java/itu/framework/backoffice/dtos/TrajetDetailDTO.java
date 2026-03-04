package itu.framework.backoffice.dtos;

import itu.framework.backoffice.entities.Trajet;
import itu.framework.backoffice.entities.TrajetReservation;
import itu.framework.backoffice.entities.Vehicule;
import itu.framework.backoffice.entities.Reservation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class TrajetDetailDTO implements Serializable {
    private Integer trajetId;
    private String vehiculeRef;
    private Integer nbrPlace;
    private List<ReservationTrajetDTO> reservations;
    private Integer nbPassagersTotal; // somme de tous les passagers
    private LocalDateTime heureDepart;
    private LocalDateTime heureArrivee;
    private BigDecimal distanceTotale;
    private List<String> ordreVisites; // ["AERO01", "HOT001", "HOT002", "AERO01"]
    private String detailTrajetFormate; // "Aéroport → Hilton → Carlton → Aéroport"

    public TrajetDetailDTO() {
        // Constructeur par défaut
    }

    public TrajetDetailDTO(Trajet trajet, List<TrajetReservation> liens) {
        try {
            this.trajetId = trajet.getId();
            this.heureDepart = trajet.getHeureDepart();
            this.heureArrivee = trajet.getHeureArrivee();
            this.distanceTotale = trajet.getDistanceTotale();

            // Récupérer le véhicule
            Vehicule vehicule = (Vehicule) trajet.getForeignKey("id_vehicule");
            if (vehicule != null) {
                this.vehiculeRef = vehicule.getRef();
                this.nbrPlace = vehicule.getNbrPlace();
            }

            // Traiter les réservations
            this.reservations = new ArrayList<>();
            this.nbPassagersTotal = 0;

            if (liens != null) {
                for (TrajetReservation lien : liens) {
                    try {
                        Reservation reservation = (Reservation) lien.getForeignKey("id_reservation");
                        if (reservation != null) {
                            ReservationTrajetDTO resDto = new ReservationTrajetDTO(reservation);
                            this.reservations.add(resDto);
                            this.nbPassagersTotal += reservation.getNbPassager();
                        }
                    } catch (Exception e) {
                        // Ignorer cette réservation en cas d'erreur
                    }
                }
            }

            // Traiter l'ordre des visites
            if (trajet.getOrdreVisites() != null && !trajet.getOrdreVisites().isEmpty()) {
                String[] codes = trajet.getOrdreVisites().split(",");
                this.ordreVisites = new ArrayList<>();
                for (String code : codes) {
                    this.ordreVisites.add(code.trim());
                }

                // Formater le détail du trajet
                this.detailTrajetFormate = String.join(" → ", this.ordreVisites);
            } else {
                this.ordreVisites = new ArrayList<>();
                this.detailTrajetFormate = "Trajet non défini";
            }

        } catch (Exception e) {
            // Initialisation de base en cas d'erreur
            this.trajetId = trajet.getId();
            this.vehiculeRef = "Véhicule inconnu";
            this.nbrPlace = 0;
            this.reservations = new ArrayList<>();
            this.nbPassagersTotal = 0;
            this.heureDepart = trajet.getHeureDepart();
            this.heureArrivee = trajet.getHeureArrivee();
            this.distanceTotale = trajet.getDistanceTotale();
            this.ordreVisites = new ArrayList<>();
            this.detailTrajetFormate = "Erreur lors du chargement";
        }
    }

    // Méthodes helpers
    public String getFormattedDepart() {
        if (heureDepart != null) {
            return heureDepart.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }

    public String getFormattedArrivee() {
        if (heureArrivee != null) {
            return heureArrivee.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }

    public String getFormattedDuree() {
        if (heureDepart != null && heureArrivee != null) {
            Duration duration = Duration.between(heureDepart, heureArrivee);
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            return hours + "h " + minutes + "min";
        }
        return "";
    }

    // Getters et Setters
    public Integer getTrajetId() {
        return trajetId;
    }

    public void setTrajetId(Integer trajetId) {
        this.trajetId = trajetId;
    }

    public String getVehiculeRef() {
        return vehiculeRef;
    }

    public void setVehiculeRef(String vehiculeRef) {
        this.vehiculeRef = vehiculeRef;
    }

    public Integer getNbrPlace() {
        return nbrPlace;
    }

    public void setNbrPlace(Integer nbrPlace) {
        this.nbrPlace = nbrPlace;
    }

    public List<ReservationTrajetDTO> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationTrajetDTO> reservations) {
        this.reservations = reservations;
    }

    public Integer getNbPassagersTotal() {
        return nbPassagersTotal;
    }

    public void setNbPassagersTotal(Integer nbPassagersTotal) {
        this.nbPassagersTotal = nbPassagersTotal;
    }

    public LocalDateTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public BigDecimal getDistanceTotale() {
        return distanceTotale;
    }

    public void setDistanceTotale(BigDecimal distanceTotale) {
        this.distanceTotale = distanceTotale;
    }

    public List<String> getOrdreVisites() {
        return ordreVisites;
    }

    public void setOrdreVisites(List<String> ordreVisites) {
        this.ordreVisites = ordreVisites;
    }

    public String getDetailTrajetFormate() {
        return detailTrajetFormate;
    }

    public void setDetailTrajetFormate(String detailTrajetFormate) {
        this.detailTrajetFormate = detailTrajetFormate;
    }
}


