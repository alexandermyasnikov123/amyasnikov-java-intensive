package net.dunice.intensive.spring_boot.dtos.responses;

import java.util.Set;

public record QuizResponse(
        Long id,
        String title,
        String description,
        String rightAnswer,
        Set<String> others,
        Integer difficulty,
        Set<String> images
) {
}
