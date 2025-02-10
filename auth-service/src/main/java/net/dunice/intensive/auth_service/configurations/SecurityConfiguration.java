package net.dunice.intensive.auth_service.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Import(value = JdbcUserDetailsManager.class)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final SecurityProperties properties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(properties.getIgnoring());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorization -> {
            authorization
                    .requestMatchers(properties.getFullPermitted()).permitAll()
                    .requestMatchers(properties.getRequired()).authenticated();
        }).build();
    }

    @Bean
    @Primary
    public UserDetailsManager userDetailsManager(JdbcUserDetailsManager manager) {
        return manager;
    }

    @Bean
    @Primary
    public GroupManager groupManager(JdbcUserDetailsManager manager) {
        return manager;
    }
}
