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

    @GetMapping("/test")
    @Json
    public String test(@RequestBody RequestDto requestDto) {
        return "Message recu: "+requestDto.getMessage()+" avec le token: "+requestDto.getToken();
    }
    
    private static class RequestDto {
        String token;
        String message;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
