package itu.framework.backoffice.models;

import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.Vehicule;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TrajetCandidat {
    Vehicule vehicule;
    List<Reservation> reservations;
    LocalDateTime heureDepart;
    LocalDateTime heureArrivee;
    BigDecimal distanceTotale;
    List<String> ordreVisites;

    public Integer getTempsAttenteRestant() {
        if(heureDepart == null || reservations.size() == 0) {
            return 0;
        }
        Long minutesPasseesEntrePremiereReservationEtHeureDepart = Duration.between(reservations.get(0).getDateHeureArrivee(), heureDepart).toMinutes();
        Integer minutesRestantes = reservations.get(0).getTempsAttenteMaxEffectif() - Math.toIntExact(minutesPasseesEntrePremiereReservationEtHeureDepart);
        if(minutesRestantes > 30) {
            System.out.println("[DEBUG TEMPS RESTANT]: minutes passess entre premier et heure dep: "+minutesPasseesEntrePremiereReservationEtHeureDepart);
            System.out.println("[DEBUG TEMPS RESTANT]: temps attente max effectif: "+reservations.get(0).getTempsAttenteMaxEffectif());
            System.out.println("[DEBUG TEMPS RESTANT]: minutes restantes: "+minutesRestantes);
        }
        return minutesRestantes > 0 ? minutesRestantes : 0;
    }

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
