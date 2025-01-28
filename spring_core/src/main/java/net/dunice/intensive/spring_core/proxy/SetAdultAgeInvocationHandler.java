package net.dunice.intensive.spring_core.proxy;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dunice.intensive.spring_core.beans.AgeOwner;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class SetAdultAgeInvocationHandler implements InvocationHandler {
    private final AgeOwner ageOwner;

    @Override
    @SneakyThrows
    public Object invoke(
            Object proxy,
            Method method,
            Object[] args
    ) {
        if (method.getName().equals("setAge")) {
            final var age = (Integer) args[0];
            final var newAge = age < 18 ? 18 : age;

            System.out.println("AgeOwner.setAge(" + age + ") is intercepted, will be called with age = " + newAge);
            return method.invoke(ageOwner, newAge);
        }
        return method.invoke(ageOwner, args);
    }
}
