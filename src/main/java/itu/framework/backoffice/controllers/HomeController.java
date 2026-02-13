package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.*;
import itu.framework.backoffice.dtos.RequestDTO;
import itu.framework.backoffice.entities.TokenClient;

import java.time.LocalDateTime;

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

    @GetMapping("/protected")
    @Json
    public String getProtected(@RequestBody RequestDTO body) {
        boolean validation = TokenClient.isTokenValid(body.getToken(), LocalDateTime.now());
        if (validation) return "success";
        return "failed";
    }

}
