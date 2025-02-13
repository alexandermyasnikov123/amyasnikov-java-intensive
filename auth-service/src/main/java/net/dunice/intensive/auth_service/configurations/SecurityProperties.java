package net.dunice.intensive.auth_service.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service.security")
public class SecurityProperties {
    private String[] ignoring;

    private String[] fullPermitted;

    private String[] required;
}

