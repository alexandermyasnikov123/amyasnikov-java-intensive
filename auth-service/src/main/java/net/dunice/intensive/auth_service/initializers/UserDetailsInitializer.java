package net.dunice.intensive.auth_service.initializers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsInitializer {
    private final UserDetailsManager manager;

    @PostConstruct
    public void initialize() {

    }
}
