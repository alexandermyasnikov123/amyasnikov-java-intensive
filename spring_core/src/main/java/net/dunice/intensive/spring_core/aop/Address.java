package net.dunice.intensive.spring_core.aop;

import lombok.Data;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class Address {
    private String city;

    private String street;

    private int code;
}
