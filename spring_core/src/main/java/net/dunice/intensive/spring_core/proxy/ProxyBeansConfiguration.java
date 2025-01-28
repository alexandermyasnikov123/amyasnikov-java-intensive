package net.dunice.intensive.spring_core.proxy;

import net.dunice.intensive.spring_core.beans.AgeOwner;
import net.dunice.intensive.spring_core.beans.MutableUserData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.lang.reflect.Proxy;

@Configuration
public class ProxyBeansConfiguration {

    @Bean
    public AgeOwner proxyMutableUserData() {
        final var instance = new MutableUserData();
        final var clazz = instance.getClass();
        final var result = (AgeOwner) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                clazz.getInterfaces(),
                new SetAdultAgeInvocationHandler(instance)
        );
        result.setAge(10);
        return result;
    }
}
