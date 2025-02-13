package net.dunice.intensive.auth_service.initializers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.auth_service.configurations.UsersProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsInitializer {
    private final UsersProperties properties;

    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    @PostConstruct
    public void initialize() {
        properties.getPredefined()
                .stream()
                .filter(userData -> !manager.userExists(userData.getUsername()))
                .forEach(userData -> manager.createUser(
                        User.builder()
                                .passwordEncoder(encoder::encode)
                                .username(userData.getUsername())
                                .password(userData.getPassword())
                                .roles()
                                .build()
                ));
    }
}
