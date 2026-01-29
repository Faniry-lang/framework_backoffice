package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.*;
import com.itu.framework.view.*;

@Controller("/")
public class HelloWorldController {
    @GetMapping
    public ModelView sayHello() {
        ModelView mv = new ModelView("hello");
        return mv;
    }
}
