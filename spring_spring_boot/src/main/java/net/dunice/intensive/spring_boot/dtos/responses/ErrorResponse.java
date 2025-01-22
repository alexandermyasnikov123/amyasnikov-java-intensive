package net.dunice.intensive.spring_boot.dtos.responses;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public record ErrorResponse(
        Map<String, Set<String>> errors,
        Set<String> causes,
        Date timeStamp
) {
    public static ErrorResponse fromErrors(Map<String, Set<String>> errors) {
        return new ErrorResponse(errors, errors.keySet(), new Date());
    }
}
