package itu.framework.backoffice.controllers;

import com.itu.framework.annotations.Controller;
import com.itu.framework.annotations.GetMapping;
import com.itu.framework.annotations.RequestParam;
import com.itu.framework.view.ModelView;

@Controller("/error")
public class ErrorController {
    @GetMapping
    public ModelView showErrorPage(@RequestParam("error-message") String errorMessage,
                                   @RequestParam("link") String link) {
        ModelView errorView = new ModelView("error");
        errorView.addObject("error-message", errorMessage);
        errorView.addObject("link", link);
        return errorView;
    }
}
