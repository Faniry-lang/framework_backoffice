package itu.framework.backoffice;

import com.itu.framework.FrameworkRunner;
import com.itu.framework.annotations.FrameworkApplication;
import itu.framework.backoffice.dtos.ReservationDTO;
import itu.framework.backoffice.entities.Distance;
import itu.framework.backoffice.entities.Reservation;
import itu.framework.backoffice.entities.Trajet;
import itu.framework.backoffice.entities.TrajetReservation;
import itu.framework.backoffice.models.AssignmentResult;
import itu.framework.backoffice.services.AssignmentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@FrameworkApplication()
public class Main {
    public static void main(String[] args) throws Exception {
//        AssignmentService service = new AssignmentService();
//        AssignmentResult results = service.assignVehicles(LocalDate.now());

        FrameworkRunner.run(Main.class, args);
    }
}
























