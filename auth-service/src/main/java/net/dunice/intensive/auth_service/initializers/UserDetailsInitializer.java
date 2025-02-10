package net.dunice.intensive.auth_service.initializers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.auth_service.configurations.UsersProperties;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsInitializer {
    private final UsersProperties properties;

    private final UserDetailsManager manager;

    @PostConstruct
    public void initialize() {

    }
}
