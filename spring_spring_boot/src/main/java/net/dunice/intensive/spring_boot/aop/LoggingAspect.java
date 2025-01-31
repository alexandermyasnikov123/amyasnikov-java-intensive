package net.dunice.intensive.spring_boot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Pointcut(value = "within(net.dunice.intensive.spring_boot.services.*.*ImagesService*)")
    public void withinAnyImagesService() {
    }

    @Pointcut(value = "execution(long *delete*(java.util.List))")
    public void deletionExecution() {
    }

    @Pointcut(value = "withinAnyImagesService() && deletionExecution()")
    public void withinAnyImagesDeletion() {
    }

    @SuppressWarnings("unchecked")
    @AfterReturning(pointcut = "withinAnyImagesDeletion()", returning = "deleted")
    public void afterDeletionReturning(JoinPoint joinPoint, long deleted) {
        final var inputUrls = (List<String>) Arrays.asList(joinPoint.getArgs()).getFirst();
        if (inputUrls.size() != deleted) {
            log.warn(
                    "Not all inputUrls were deleted. Deleted - {}, Urls - {} : {}",
                    deleted,
                    inputUrls.size(),
                    inputUrls
            );
        }
    }
}
