package net.dunice.intensive.spring_core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomAspect {

    @Pointcut(value = "execution(public * net.dunice.intensive.spring_core.aop.*.set*(..)) && !@annotation(org.springframework.stereotype.Component)")
    public void anyMutablePointcut() {
    }

    @Pointcut(value = "execution(public Address set*(..)) && @annotation(org.springframework.stereotype.Component)")
    public void anyAddressSetterPointcut() {
    }

    @Before(value = "anyMutablePointcut()")
    public void before(JoinPoint joinPoint) {
        System.out.println(joinPoint);
    }
}
