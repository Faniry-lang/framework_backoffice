package itu.framework.backoffice.models;

import itu.framework.backoffice.dtos.ReservationDTO;
import itu.framework.backoffice.entities.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public class GroupeReservation {
    List<ReservationDTO> reservations;
    LocalDateTime heureArriveeVehicule;

    public List<ReservationDTO> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationDTO> reservations) {
        this.reservations = reservations;
    }

    public LocalDateTime getHeureArriveeVehicule() {
        return heureArriveeVehicule;
    }

    public void setHeureArriveeVehicule(LocalDateTime heureArriveeVehicule) {
        this.heureArriveeVehicule = heureArriveeVehicule;
    }
}
