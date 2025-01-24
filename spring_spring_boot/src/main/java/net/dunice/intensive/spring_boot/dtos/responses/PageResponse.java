package net.dunice.intensive.spring_boot.dtos.responses;

import java.util.Collection;

public record PageResponse<T>(
        Collection<T> data,
        Long numberOfElements,
        Long totalElements
) {
}
