package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.*;

@Controller("/")
public class HelloWorldController {
    @GET
    @UrlMapping("/")
    @Json
    public ModelView sayHello() {
        ModelView mv = new ModelView();
        mv.addObject("message", "Hello from Heroku!");
        mv.addObject("name", "Faniry");
        mv.addObject("status", "running");
        return mv;
    }
}
