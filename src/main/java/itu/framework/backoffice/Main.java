package itu.framework.backoffice;

import com.itu.framework.FrameworkRunner;
import com.itu.framework.annotations.FrameworkApplication;
import itu.framework.backoffice.models.AssignmentResult;
import itu.framework.backoffice.services.AssignmentService;

import java.time.LocalDate;

@FrameworkApplication()
public class Main {
    public static void main(String[] args) throws Exception {
//         AssignmentService service = new AssignmentService();
//         AssignmentResult results = service.assignVehicles(LocalDate.now(), null, null);
//
       FrameworkRunner.run(Main.class, args);
    }
}
