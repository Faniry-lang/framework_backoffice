package itu.framework.backoffice;

import com.itu.framework.FrameworkRunner;
import com.itu.framework.annotations.FrameworkApplication;

@FrameworkApplication()
public class Main {
    public static void main(String[] args) throws Exception {
       FrameworkRunner.run(Main.class, args);
    }
}
