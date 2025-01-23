package net.dunice.intensive.spring_boot.dtos.shared;

import net.dunice.intensive.spring_boot.entities.AnswerEntity;
import net.dunice.intensive.spring_boot.entities.QuizEntity;
import net.dunice.intensive.spring_boot.validations.fields.ValidField;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import java.util.Set;

public record QuizDto(
        @ValidField(min = QuizEntity.TITLE_MIN_LENGTH, max = QuizEntity.TITLE_MAX_LENGTH)
        String title,
        @ValidField(min = QuizEntity.DESCRIPTION_MIN_LENGTH, max = QuizEntity.DESCRIPTION_MAX_LENGTH)
        String description,
        @ValidField(min = AnswerEntity.MIN_ANSWER_LENGTH, max = AnswerEntity.MAX_ANSWER_LENGTH)
        String rightAnswer,
        Set<@ValidField(min = AnswerEntity.MIN_ANSWER_LENGTH, max = AnswerEntity.MAX_ANSWER_LENGTH) String> others,
        @Range(min = QuizEntity.MIN_DIFFICULTY, max = QuizEntity.MAX_DIFFICULTY)
        Integer difficulty,
        Set<@URL String> images
) {
}
