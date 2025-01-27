package net.dunice.intensive.spring_core.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SomeConfiguration {

    @Bean
    public String someString() {
        return "some string value";
    }

    @Bean
    public String anotherString() {
        return "another string value";
    }
}
