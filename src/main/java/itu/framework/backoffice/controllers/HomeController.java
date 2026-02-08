package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.*;

@Controller("/")
public class HomeController {
    @GetMapping
    public ModelView showHomePage() {
        ModelView mv = new ModelView("home");
        return mv;
    }

    @GetMapping("/health")
    @Json
    public String healthCheck() {
        return "Ok";
    }
}
