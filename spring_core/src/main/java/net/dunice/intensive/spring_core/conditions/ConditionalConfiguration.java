package net.dunice.intensive.spring_core.conditions;

import net.dunice.intensive.spring_core.beans.MutableUserData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConditionalConfiguration {

    @Bean
    @Conditional(value = HasNotMutableUserDataDefinition.class)
    public MutableUserData mutableUserData() {
        throwNull();
        return new MutableUserData();
    }

    private void throwNull() {
        throw null;
    }
}
