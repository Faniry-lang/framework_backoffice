package itu.framework.backoffice.dtos;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReservationDTO implements Serializable {
    Integer nb_passager;
    String id_client;
    String nom_hotel;
    Timestamp date_reservation;

    public Integer getNb_passager() {
        return nb_passager;
    }

    public void setNb_passager(Integer nb_passager) {
        this.nb_passager = nb_passager;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getNom_hotel() {
        return nom_hotel;
    }

    public void setNom_hotel(String nom_hotel) {
        this.nom_hotel = nom_hotel;
    }

    public Timestamp getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(Timestamp date_reservation) {
        this.date_reservation = date_reservation;
    }
}
