package net.dunice.intensive.auth_service.configurations;

import lombok.RequiredArgsConstructor;
import net.dunice.intensive.auth_service.filters.SecurityCheckerFilter;
import org.jose4j.jwe.JsonWebEncryption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.sql.DataSource;

@Import(value = {BCryptPasswordEncoder.class, JsonWebEncryption.class})
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final SecurityProperties properties;

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource jdbcDataSource) {
        return new JdbcUserDetailsManager(jdbcDataSource);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(properties.getIgnoring());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityCheckerFilter filter
    ) throws Exception {
        return http.authorizeHttpRequests(matchers -> matchers
                        .requestMatchers(properties.getFullPermitted()).permitAll()
                        .requestMatchers(properties.getRequired()).authenticated())
                .formLogin(FormLoginConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
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
