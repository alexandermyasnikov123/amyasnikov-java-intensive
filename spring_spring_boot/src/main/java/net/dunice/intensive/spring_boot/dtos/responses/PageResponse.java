package net.dunice.intensive.spring_boot.dtos.responses;

import java.util.Collection;

public record PageResponse<T>(
        Collection<T> data,
        Long numberOfElements,
        Long totalElements
) {
    public static final int MIN_PAGE_NUMBER = 1;

    public static final int MIN_PAGE_SIZE = 1;

    public static final int MAX_PAGE_SIZE = 100;
}
