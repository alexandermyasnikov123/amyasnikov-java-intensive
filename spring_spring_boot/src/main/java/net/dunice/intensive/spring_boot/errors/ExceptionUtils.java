package net.dunice.intensive.spring_boot.errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtils {

    public static Map<String, Set<String>> extractMessagesForParameters(HandlerMethodValidationException exception) {
        final Function<ParameterValidationResult, String> keyMapper = result -> result
                .getMethodParameter()
                .getParameterName();

        final Function<ParameterValidationResult, Set<String>> valueMapper = result -> result
                .getResolvableErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());

        return exception.getParameterValidationResults()
                .stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static Map<String, Set<String>> extractMessagesForParameters(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet()))
                );
    }
}
