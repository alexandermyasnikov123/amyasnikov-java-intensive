package net.dunice.intensive.spring_boot.errors;

import net.dunice.intensive.spring_boot.dtos.responses.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentValidationExceptions(MethodArgumentNotValidException exception) {
        final var errorsMap = exception.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toSet()))
                );

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.fromErrors(errorsMap));
    }
}
