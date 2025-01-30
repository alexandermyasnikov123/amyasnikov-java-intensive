package net.dunice.intensive.spring_boot.dtos.responses;

import java.net.URI;

public record CreationResponse<T>(
        T data,
        URI location
) {
}
