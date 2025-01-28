package net.dunice.intensive.spring_core.conditions;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import java.util.Objects;

public class HasNotMutableUserDataDefinition implements Condition {

    @Override
    public boolean matches(
            @NotNull
            ConditionContext context,
            @NotNull
            AnnotatedTypeMetadata metadata
    ) {
        return !Objects.requireNonNull(context.getBeanFactory())
                .containsBeanDefinition("mutableUserData");
    }
}
