package net.dunice.intensive.spring_boot.errors;

import net.dunice.intensive.spring_boot.dtos.responses.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handle(HandlerMethodValidationException exception) {
        final var errors = ExceptionUtils.extractMessagesForParameters(exception);
        return ResponseEntity.badRequest().body(ErrorResponse.fromErrors(errors));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentValidationExceptions(MethodArgumentNotValidException exception) {
        final var errorsMap = ExceptionUtils.extractMessagesForMethodParameters(exception);
        return ResponseEntity.badRequest().body(ErrorResponse.fromErrors(errorsMap));
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        final var errors = Map.of(exception.getName(), Set.of(exception.getMessage()));
        return ResponseEntity.badRequest().body(ErrorResponse.fromErrors(errors));
    }
}
