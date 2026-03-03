package itu.framework.backoffice.helpers;

import itu.framework.backoffice.entities.Trajet;
import itu.framework.backoffice.entities.Reservation;

import java.util.List;
import java.util.ArrayList;

public class AssignmentResult {
    private List<Trajet> trajetsCreated;
    private List<Reservation> reservationsNonAssignees;

    public AssignmentResult() {
        this.trajetsCreated = new ArrayList<>();
        this.reservationsNonAssignees = new ArrayList<>();
    }

    public AssignmentResult(List<Trajet> trajetsCreated, List<Reservation> reservationsNonAssignees) {
        this.trajetsCreated = trajetsCreated;
        this.reservationsNonAssignees = reservationsNonAssignees;
    }

    // Getters et Setters
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
}
