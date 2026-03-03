package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.ModelView;
import itu.framework.backoffice.dtos.TrajetDetailDTO;
import itu.framework.backoffice.dtos.ReservationNonAssigneeDTO;
import itu.framework.backoffice.entities.Trajet;
import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.TrajetReservation;
import itu.framework.backoffice.helpers.AssignmentService;
import itu.framework.backoffice.helpers.AssignmentResult;

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
            // 1. Récupérer date du paramètre (parse en LocalDate)
            LocalDate dateLocalDate = LocalDate.parse(date);

            // 2. Exécuter AssignmentService.assignVehicles(date)
            AssignmentResult result = AssignmentService.assignVehicles(dateLocalDate);

            // 3. Récupérer tous trajets créés pour cette date via Trajet.findByDate(date)
            List<Trajet> trajets = Trajet.findByDate(dateLocalDate);
            List<TrajetDetailDTO> trajetsDetails = new ArrayList<>();

            // 4. Pour chaque trajet:
            for (Trajet trajet : trajets) {
                // - Récupérer véhicule
                // - Récupérer réservations via TrajetReservation.findByTrajet(trajet.getId())
                List<TrajetReservation> liens = TrajetReservation.findByTrajet(trajet.getId());

                // - Créer TrajetDetailDTO
                TrajetDetailDTO dto = new TrajetDetailDTO(trajet, liens);
                trajetsDetails.add(dto);
            }

            // 5. Récupérer réservations non assignées via Reservation.findUnassignedByDate(date)
            List<Reservation> reservationsNonAssigneesList = Reservation.findUnassignedByDate(dateLocalDate);

            // 6. Créer ReservationNonAssigneeDTO pour chaque
            List<ReservationNonAssigneeDTO> reservationsNonAssignees = new ArrayList<>();
            for (Reservation reservation : reservationsNonAssigneesList) {
                ReservationNonAssigneeDTO dto = new ReservationNonAssigneeDTO(reservation);
                reservationsNonAssignees.add(dto);
            }

            // 7. Passer en attributs
            ModelView mv = new ModelView("planification/planification-view");
            mv.addObject("trajetsDetails", trajetsDetails);
            mv.addObject("reservationsNonAssignees", reservationsNonAssignees);
            mv.addObject("dateSelectionnee", dateLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            // 8. Afficher planification-view.jsp
            return mv;

        } catch (Exception e) {
            return ModelView.redirect("/error?error-message=" + e.getMessage() + "&link=/api/planification/form");
        }
    }
}

