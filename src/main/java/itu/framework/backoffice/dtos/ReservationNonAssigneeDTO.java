package itu.framework.backoffice.dtos;

import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.Lieux;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationNonAssigneeDTO implements Serializable {
    private Integer reservationId;
    private String idClient;
    private Integer nbPassager;
    private String hotelNom;
    private LocalDateTime heureArriveeClient;
    private Integer tempsAttenteMax;

    public ReservationNonAssigneeDTO() {
        // Constructeur par défaut
    }

    public ReservationNonAssigneeDTO(Reservation reservation) {
        try {
            this.reservationId = reservation.getId();
            this.idClient = reservation.getIdClient();
            this.nbPassager = reservation.getNbPassager();
            this.heureArriveeClient = reservation.getDateHeureArrivee();
            this.tempsAttenteMax = reservation.getTempsAttenteMax() != null ? reservation.getTempsAttenteMax() : 30;

            // Récupérer l'hôtel via la clé étrangère
            Lieux hotel = (Lieux) reservation.getForeignKey("id_hotel");
            if (hotel != null) {
                this.hotelNom = hotel.getNom();
            } else {
                this.hotelNom = "Hôtel inconnu";
            }
        } catch (Exception e) {
            // En cas d'erreur, initialiser avec des valeurs par défaut
            this.reservationId = reservation.getId();
            this.idClient = reservation.getIdClient();
            this.nbPassager = reservation.getNbPassager();
            this.heureArriveeClient = reservation.getDateHeureArrivee();
            this.tempsAttenteMax = 30; // valeur par défaut
            this.hotelNom = "Hôtel inconnu";
        }
    }

    // Méthode helper
    public String getFormattedHeure() {
        if (heureArriveeClient != null) {
            return heureArriveeClient.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }

    // Getters et Setters
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Integer getNbPassager() {
        return nbPassager;
    }

    public void setNbPassager(Integer nbPassager) {
        this.nbPassager = nbPassager;
    }

    public String getHotelNom() {
        return hotelNom;
    }

    public void setHotelNom(String hotelNom) {
        this.hotelNom = hotelNom;
    }

    public LocalDateTime getHeureArriveeClient() {
        return heureArriveeClient;
    }

    public void setHeureArriveeClient(LocalDateTime heureArriveeClient) {
        this.heureArriveeClient = heureArriveeClient;
    }

    public Integer getTempsAttenteMax() {
        return tempsAttenteMax;
    }

    public void setTempsAttenteMax(Integer tempsAttenteMax) {
        this.tempsAttenteMax = tempsAttenteMax;
    }
}
