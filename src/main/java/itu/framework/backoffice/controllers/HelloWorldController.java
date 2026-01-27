package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.*;

@Controller("/")
public class HelloWorldController {
    @GET
    @UrlMapping
    public ModelView sayHello() {
        ModelView mv = new ModelView("hello");
        mv.addObject("name", "Faniry");
        return mv;
    }
}
