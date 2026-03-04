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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            // Trajets planifiés (en base de données)
            List<Trajet> trajetsPlanifies = Trajet.findBy("date_trajet", dateLocalDate, Trajet.class);
            List<TrajetDetailDTO> trajetsPlanifiesDetails = new ArrayList<>();

            for (Trajet trajet : trajetsPlanifies) {
                List<TrajetReservation> liens = TrajetReservation.findByTrajet(trajet.getId());
                TrajetDetailDTO dto = new TrajetDetailDTO(trajet, liens);
                trajetsPlanifiesDetails.add(dto);
            }

            // Trajets générés (non persistés)
            List<Trajet> trajetsGeneres = result.getTrajetsCreated();
            Map<Trajet, TrajetReservation[]> trajetReservationsMap = result.getTrajetReservations();
            List<TrajetDetailDTO> trajetsGeneresDetails = new ArrayList<>();

            for (Trajet trajet : trajetsGeneres) {
                TrajetReservation[] liens = trajetReservationsMap.get(trajet);
                List<TrajetReservation> liensList = new ArrayList<>();
                if (liens != null) {
                    for (TrajetReservation tr : liens) {
                        liensList.add(tr);
                    }
                }
                TrajetDetailDTO dto = new TrajetDetailDTO(trajet, liensList);
                trajetsGeneresDetails.add(dto);
            }

            List<Reservation> reservationsNonAssigneesList = result.getReservationsNonAssignees();
            List<ReservationNonAssigneeDTO> reservationsNonAssignees = new ArrayList<>();
            for (Reservation reservation : reservationsNonAssigneesList) {
                ReservationNonAssigneeDTO dto = new ReservationNonAssigneeDTO(reservation);
                reservationsNonAssignees.add(dto);
            }

            ModelView mv = new ModelView("planification/planification-view");
            mv.addObject("trajetsPlanifies", trajetsPlanifiesDetails);
            mv.addObject("trajetsGeneres", trajetsGeneresDetails);
            mv.addObject("trajetReservationsMap", trajetReservationsMap);
            mv.addObject("reservationsNonAssignees", reservationsNonAssignees);
            mv.addObject("dateSelectionnee", dateLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            mv.addObject("dateValue", date);

            return mv;

        } catch (Exception e) {
            return ModelView.redirect("/error?error-message=" + e.getMessage() + "&link=/api/planification/form");
        }
    }

    @PostMapping("/save")
    @SuppressWarnings("unchecked")
    public ModelView saveTrajet(@RequestBody Map<String, Object> requestData) {
        try {
            // Extraction des données
            Map<String, Object> trajetData = (Map<String, Object>) requestData.get("trajet");
            List<Map<String, Object>> reservationsData = (List<Map<String, Object>>) requestData.get("reservations");
            String date = (String) requestData.get("date");

            // Création du trajet
            Trajet trajet = new Trajet();
            trajet.setIdVehicule((Integer) trajetData.get("idVehicule"));
            trajet.setDateTrajet(LocalDate.parse((String) trajetData.get("dateTrajet")));

            // Parse heures from String to LocalDateTime
            String heureDepart = (String) trajetData.get("heureDepart");
            String heureArrivee = (String) trajetData.get("heureArrivee");
            trajet.setHeureDepart(LocalDateTime.parse(heureDepart));
            trajet.setHeureArrivee(LocalDateTime.parse(heureArrivee));

            // Convert Double to BigDecimal
            Object distanceObj = trajetData.get("distanceTotale");
            BigDecimal distance = distanceObj instanceof Double
                    ? BigDecimal.valueOf((Double) distanceObj)
                    : new BigDecimal(distanceObj.toString());
            trajet.setDistanceTotale(distance);
            trajet.setOrdreVisites((String) trajetData.get("ordreVisites"));

            // Sauvegarde du trajet
            trajet = (Trajet) trajet.save();

            // Sauvegarde des TrajetReservation
            for (Map<String, Object> resData : reservationsData) {
                TrajetReservation tr = new TrajetReservation();
                tr.setIdTrajet(trajet.getId().intValue());
                tr.setIdReservation((Integer) resData.get("idReservation"));
                tr.setOrdreVisite((Integer) resData.get("ordreVisite"));
                tr.save();
            }

            return ModelView.redirect("/api/planification/assign?date=" + date);

        } catch (Exception e) {
            return ModelView.redirect("/error?error-message=" + e.getMessage() + "&link=/api/planification/form");
        }
    }

    @PostMapping("/save-all")
    @SuppressWarnings("unchecked")
    public ModelView saveAllTrajets(@RequestBody Map<String, Object> requestData) {
        try {
            List<Map<String, Object>> trajetsData = (List<Map<String, Object>>) requestData.get("trajets");
            String date = (String) requestData.get("date");

            for (Map<String, Object> trajetEntry : trajetsData) {
                Map<String, Object> trajetData = (Map<String, Object>) trajetEntry.get("trajet");
                List<Map<String, Object>> reservationsData = (List<Map<String, Object>>) trajetEntry
                        .get("reservations");

                // Création du trajet
                Trajet trajet = new Trajet();
                trajet.setIdVehicule((Integer) trajetData.get("idVehicule"));
                trajet.setDateTrajet(LocalDate.parse((String) trajetData.get("dateTrajet")));

                // Parse heures from String to LocalDateTime
                String heureDepart = (String) trajetData.get("heureDepart");
                String heureArrivee = (String) trajetData.get("heureArrivee");
                trajet.setHeureDepart(LocalDateTime.parse(heureDepart));
                trajet.setHeureArrivee(LocalDateTime.parse(heureArrivee));

                // Convert Double to BigDecimal
                Object distanceObj = trajetData.get("distanceTotale");
                BigDecimal distance = distanceObj instanceof Double
                        ? BigDecimal.valueOf((Double) distanceObj)
                        : new BigDecimal(distanceObj.toString());
                trajet.setDistanceTotale(distance);
                trajet.setOrdreVisites((String) trajetData.get("ordreVisites"));

                // Sauvegarde du trajet
                trajet = (Trajet) trajet.save();

                // Sauvegarde des TrajetReservation
                for (Map<String, Object> resData : reservationsData) {
                    TrajetReservation tr = new TrajetReservation();
                    tr.setIdTrajet(trajet.getId().intValue());
                    tr.setIdReservation((Integer) resData.get("idReservation"));
                    tr.setOrdreVisite((Integer) resData.get("ordreVisite"));
                    tr.save();
                }
            }

            return ModelView.redirect("/api/planification/assign?date=" + date);

        } catch (Exception e) {
            return ModelView.redirect("/error?error-message=" + e.getMessage() + "&link=/api/planification/form");
        }
    }
}
