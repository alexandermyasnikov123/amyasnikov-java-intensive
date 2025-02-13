package net.dunice.intensive.auth_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/auth")
public class AuthController {

    /**
     * Just a gimmick end-point. The only purpose is the redirecting from other services.
     * The real auth checking will be performed here (see below):
     *
     * @see net.dunice.intensive.auth_service.filters.SecurityCheckerFilter
     **/
    @GetMapping
    public ResponseEntity<?> checkMe() {
        return ResponseEntity.ok().build();
    }
}
