package net.dunice.intensive.auth_service.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut(value = "within(net.dunice.intensive.auth_service.filters.*) && execution(public * doFilter(..))")
    public void doFilterPointcut() {
    }

    @Before(value = "doFilterPointcut()")
    @SneakyThrows
    public void beforeFilters(JoinPoint joinPoint) {
        log.info("filter: {}, args: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }
}
