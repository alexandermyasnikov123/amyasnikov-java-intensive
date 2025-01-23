package net.dunice.intensive.spring_boot.mappers;

import net.dunice.intensive.spring_boot.entities.AnswerEntity;
import org.gradle.internal.impldep.com.google.common.base.Predicates;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnswersMapper {

    @Mapping(target = "id", ignore = true)
    AnswerEntity mapToEntity(String answer, Boolean isRight);

    default Set<AnswerEntity> mapToEntities(String rightAnswer, Set<String> others) {
        final var mappedList = Stream.concat(Stream.of(rightAnswer), others.stream())
                .map(answer -> mapToEntity(answer, answer.equals(rightAnswer)))
                .collect(Collectors.toList());

        Collections.shuffle(mappedList);
        return Set.copyOf(mappedList);
    }

    default String mapRightAnswer(Set<AnswerEntity> entities) {
        return entities.stream()
                .filter(AnswerEntity::getIsRight)
                .findFirst()
                .orElseThrow()
                .getAnswer();
    }

    default Set<String> mapOthers(Set<AnswerEntity> entities) {
        return entities.stream()
                .filter(Predicates.not(AnswerEntity::getIsRight))
                .map(AnswerEntity::getAnswer)
                .collect(Collectors.toSet());
    }
}
