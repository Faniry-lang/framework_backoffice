package itu.framework.backoffice.dtos;

import itu.framework.backoffice.helpers.DateFormatHelper;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReservationDTO implements Serializable {
    Integer nb_passager;
    String id_client;
    String nom_hotel;
    String date_reservation;

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

    public String getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(String date_reservation) {
        this.date_reservation = date_reservation;
    }

    public String getFormatedDate() {
        if(this.date_reservation.isEmpty() || this.date_reservation == null) {
            return "N/A";
        }
        return DateFormatHelper.formatDate(this.date_reservation);
    }
}
