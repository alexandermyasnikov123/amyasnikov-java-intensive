package net.dunice.intensive.spring_boot.tasks;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Component
@Setter
@RequiredArgsConstructor
public class GetServices implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public Collection<Object> findAllServices() {
        return applicationContext.getBeansWithAnnotation(Service.class).values();
    }
}
