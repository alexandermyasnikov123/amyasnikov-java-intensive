package net.dunice.intensive.spring_core.beans;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class AdultBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final int ADULT_AGE = 18;

    @Override
    public Object postProcessBeforeInitialization(
            @NotNull Object bean,
            @NotNull String beanName
    ) {
        if (bean instanceof MutableUserData mutableUserData) {
            mutableUserData.setAdult(true);
            System.out.println("postProcessBeforeInitialization " + mutableUserData);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(
            @NotNull Object bean,
            @NotNull String beanName
    ) {
        if (bean instanceof MutableUserData mutableUserData &&
                mutableUserData.isAdult() &&
                mutableUserData.getAge() < ADULT_AGE
        ) {
            mutableUserData.setAge(ADULT_AGE);
            System.out.println("postProcessAfterInitialization completed " + mutableUserData);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
