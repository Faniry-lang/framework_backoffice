package itu.framework.backoffice.models;

import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.Trajet;

import java.util.List;

public class AssignmentResult {
    private List<Trajet> trajetsCreated;
    private List<Reservation> reservationsNonAssignees;

    public AssignmentResult() {
    }

    public AssignmentResult(List<Trajet> trajetsCreated, List<Reservation> reservationsNonAssignees) {
        this.trajetsCreated = trajetsCreated;
        this.reservationsNonAssignees = reservationsNonAssignees;
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
}
