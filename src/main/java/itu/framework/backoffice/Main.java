package itu.framework.backoffice;

import com.itu.framework.FrameworkRunner;
import com.itu.framework.annotations.FrameworkApplication;

@FrameworkApplication()
public class Main {
    public static void main(String[] args) throws Exception {
        // AssignmentService service = new AssignmentService();
        // AssignmentResult results = service.assignVehicles(LocalDate.now());

        FrameworkRunner.run(Main.class, args);
    }
}
