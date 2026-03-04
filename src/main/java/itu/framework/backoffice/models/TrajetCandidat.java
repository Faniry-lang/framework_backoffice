package itu.framework.backoffice.models;

import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.Vehicule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TrajetCandidat {
    Vehicule vehicule;
    List<Reservation> reservations;
    LocalDateTime heureDepart;
    LocalDateTime heureArrivee;
    BigDecimal distanceTotale;
    List<String> ordreVisites;

    public TrajetCandidat(Vehicule vehicule, List<Reservation> reservations, LocalDateTime heureDepart, LocalDateTime heureArrivee, BigDecimal distanceTotale, List<String> ordreVisites) {
        this.vehicule = vehicule;
        this.reservations = reservations;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.distanceTotale = distanceTotale;
        this.ordreVisites = ordreVisites;
    }

    public TrajetCandidat() {
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
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
}
