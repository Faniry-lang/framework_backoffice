package itu.framework.backoffice.models;

import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.Trajet;
import itu.framework.backoffice.entities.TrajetReservation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentResult {
    private List<Trajet> trajetsCreated;
    private List<Reservation> reservationsNonAssignees;
    private Map<Trajet, TrajetReservation[]> trajetReservations;

    public AssignmentResult() {
        this.trajetReservations = new HashMap<>();
    }

    public AssignmentResult(List<Trajet> trajetsCreated, List<Reservation> reservationsNonAssignees) {
        this.trajetsCreated = trajetsCreated;
        this.reservationsNonAssignees = reservationsNonAssignees;
        this.trajetReservations = new HashMap<>();
    }

    public AssignmentResult(List<Trajet> trajetsCreated, List<Reservation> reservationsNonAssignees,
            Map<Trajet, TrajetReservation[]> trajetReservations) {
        this.trajetsCreated = trajetsCreated;
        this.reservationsNonAssignees = reservationsNonAssignees;
        this.trajetReservations = trajetReservations;
    }

    public List<Trajet> getTrajetsCreated() {
        return trajetsCreated;
    }

    public void setTrajetsCreated(List<Trajet> trajetsCreated) {
        this.trajetsCreated = trajetsCreated;
    }

    public List<Reservation> getReservationsNonAssignees() {
        return reservationsNonAssignees;
    }

    public void setReservationsNonAssignees(List<Reservation> reservationsNonAssignees) {
        this.reservationsNonAssignees = reservationsNonAssignees;
    }

    public Map<Trajet, TrajetReservation[]> getTrajetReservations() {
        return trajetReservations;
    }

    public void setTrajetReservations(Map<Trajet, TrajetReservation[]> trajetReservations) {
        this.trajetReservations = trajetReservations;
    }
}
