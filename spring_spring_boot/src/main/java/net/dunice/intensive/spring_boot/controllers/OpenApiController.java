package net.dunice.intensive.spring_boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/open-api", "/swagger", "/swagger-ui", "/docs"})
public class OpenApiController {

    @GetMapping
    public String openSwaggerUi() {
        return "forward:/swagger-ui.html";
    }
}
