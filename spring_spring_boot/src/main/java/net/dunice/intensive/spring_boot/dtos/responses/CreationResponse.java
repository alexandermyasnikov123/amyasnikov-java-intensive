package net.dunice.intensive.spring_boot.dtos.responses;

public record CreationResponse<T>(
        T data,
        String location
) {
}
