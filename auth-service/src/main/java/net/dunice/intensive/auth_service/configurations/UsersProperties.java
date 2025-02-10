package net.dunice.intensive.auth_service.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "users")
public class UsersProperties {
    private List<UserProperty> predefined;

    private List<String> roles;

    @Data
    @Setter(value = AccessLevel.PACKAGE)
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static class UserProperty {
        private String username;

        private String password;

        private List<String> role;
    }
}
