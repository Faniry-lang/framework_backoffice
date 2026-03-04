package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.ModelView;
import itu.framework.backoffice.dtos.TrajetDetailDTO;
import itu.framework.backoffice.dtos.ReservationNonAssigneeDTO;
import itu.framework.backoffice.entities.Trajet;
import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.TrajetReservation;
import itu.framework.backoffice.models.AssignmentResult;
import itu.framework.backoffice.services.AssignmentService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller("/api/planification")
public class PlanificationController {

    @GetMapping("/form")
    public ModelView showForm() {
        ModelView mv = new ModelView("planification/planification-form");
        return mv;
    }

    @PostMapping("/assign")
    public ModelView assignVehicles(String date) throws Exception {
        try {
            LocalDate dateLocalDate = LocalDate.parse(date);
            AssignmentService assignmentService = new AssignmentService();
            AssignmentResult result = assignmentService.assignVehicles(dateLocalDate);
            List<Trajet> trajets = result.getTrajetsCreated();
            if(trajets.isEmpty()) {
                trajets = Trajet.findBy("date_trajet", dateLocalDate, Trajet.class);
            }
            List<TrajetDetailDTO> trajetsDetails = new ArrayList<>();

            for (Trajet trajet : trajets) {
                List<TrajetReservation> liens = TrajetReservation.findByTrajet(trajet.getId());
                TrajetDetailDTO dto = new TrajetDetailDTO(trajet, liens);
                trajetsDetails.add(dto);
            }

            List<Reservation> reservationsNonAssigneesList = Reservation.findUnassignedByDate(dateLocalDate);
            List<ReservationNonAssigneeDTO> reservationsNonAssignees = new ArrayList<>();
            for (Reservation reservation : reservationsNonAssigneesList) {
                ReservationNonAssigneeDTO dto = new ReservationNonAssigneeDTO(reservation);
                reservationsNonAssignees.add(dto);
            }

            ModelView mv = new ModelView("planification/planification-view");
            mv.addObject("trajetsDetails", trajetsDetails);
            mv.addObject("reservationsNonAssignees", reservationsNonAssignees);
            mv.addObject("dateSelectionnee", dateLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            return mv;

        } catch (Exception e) {
            return ModelView.redirect("/error?error-message=" + e.getMessage() + "&link=/api/planification/form");
        }
    }
}
