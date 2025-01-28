package net.dunice.intensive.spring_core.aop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class AddressesHandler {
    private final BeanFactory beanFactory;

    @PostConstruct
    public void createSeveralAddresses() {
        IntStream.range(1, 10)
                .mapToObj(i -> beanFactory.getBean(Address.class))
                .forEach(address -> address.setCity("aboba").setCode(123).setStreet("stttt"));
    }
}
