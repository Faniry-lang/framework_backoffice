package itu.framework.backoffice.dtos;

import itu.framework.backoffice.helpers.DateFormatHelper;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReservationDTO implements Serializable {
    Integer id;
    Integer nb_passager;
    String id_client;
    String nom_hotel;
    Integer id_hotel;
    String date_reservation;
    LocalDateTime dateHeureArrivee;
    Integer tempsAttenteMax;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getId_hotel() {
        return id_hotel;
    }

    public void setId_hotel(Integer id_hotel) {
        this.id_hotel = id_hotel;
    }

    public String getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(String date_reservation) {
        this.date_reservation = date_reservation;
    }

    public LocalDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public void setDateHeureArrivee(LocalDateTime dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }

    public String getFormatedDate() {
        if (this.date_reservation == null || this.date_reservation.isEmpty()) {
            return "N/A";
        }
        return DateFormatHelper.formatDate(this.date_reservation);
    }

    public Integer getTempsAttenteMax() {
        return tempsAttenteMax;
    }

    public void setTempsAttenteMax(Integer tempsAttenteMax) {
        this.tempsAttenteMax = tempsAttenteMax;
    }

    @Override
    public String toString() {
        return "{"
                + "\"nb_passager\":" + (nb_passager != null ? nb_passager : "null") + ","
                + "\"id_client\":\"" + (id_client != null ? id_client : "") + "\","
                + "\"nom_hotel\":\"" + (nom_hotel != null ? nom_hotel : "") + "\","
                + "\"date_reservation\":\"" + (date_reservation != null ? date_reservation : "") + "\","
                + "\"tempsAttenteMax\":" + (tempsAttenteMax != null ? tempsAttenteMax : "null")
                + "}";
    }
}
