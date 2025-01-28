package net.dunice.intensive.spring_core.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "my.own.starter")
@ConditionalOnProperty(prefix = "my.own.starter", value = "name")
public class RequiredProps {
    private String name;

    private int ttl;
}
