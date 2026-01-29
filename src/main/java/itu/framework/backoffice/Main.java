package itu.framework.backoffice;

import com.itu.framework.FrameworkRunner;
import com.itu.framework.annotations.FrameworkApplication;

@FrameworkApplication(port = 8080)
public class Main {
    public static void main(String[] args) {
        FrameworkRunner.run(Main.class, args);
    }
}