package itu.framework.backoffice.dtos;

import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.Hotel;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReservationTrajetDTO implements Serializable {
    private Integer reservationId;
    private String idClient;
    private Integer nbPassager;
    private String hotelNom;
    private String hotelCode;
    private LocalDateTime heureArriveeClient;

    public ReservationTrajetDTO() {
        // Constructeur par défaut
    }

    public ReservationTrajetDTO(Reservation reservation) {
        try {
            this.reservationId = reservation.getId();
            this.idClient = reservation.getIdClient();
            this.nbPassager = reservation.getNbPassager();
            this.heureArriveeClient = reservation.getDateHeureArrivee();

            // Récupérer l'hôtel via la clé étrangère
            Hotel hotel = (Hotel) reservation.getForeignKey("id_hotel");
            if (hotel != null) {
                this.hotelNom = hotel.getNom();
                // TODO: ajouter getCode() à l'entité Hotel si nécessaire
                this.hotelCode = "HOT" + String.format("%03d", hotel.getId());
            }
        } catch (Exception e) {
            // En cas d'erreur, initialiser avec des valeurs par défaut
            this.reservationId = reservation.getId();
            this.idClient = reservation.getIdClient();
            this.nbPassager = reservation.getNbPassager();
            this.heureArriveeClient = reservation.getDateHeureArrivee();
            this.hotelNom = "Hôtel inconnu";
            this.hotelCode = "UNKNOWN";
        }
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

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public LocalDateTime getHeureArriveeClient() {
        return heureArriveeClient;
    }

    public void setHeureArriveeClient(LocalDateTime heureArriveeClient) {
        this.heureArriveeClient = heureArriveeClient;
    }
}


