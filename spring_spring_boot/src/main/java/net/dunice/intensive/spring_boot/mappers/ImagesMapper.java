package net.dunice.intensive.spring_boot.mappers;

import net.dunice.intensive.spring_boot.entities.QuizEntity;
import net.dunice.intensive.spring_boot.entities.QuizImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImagesMapper {

    QuizImageEntity mapToEntity(String url, QuizEntity owner);

    default Set<QuizImageEntity> mapToEntities(Set<String> images, QuizEntity owner) {
        return images.stream()
                .map(image -> mapToEntity(image, owner))
                .collect(Collectors.toSet());
    }

    default Set<String> mapToUrls(Set<QuizImageEntity> entities) {
        return entities.stream()
                .map(QuizImageEntity::getUrl)
                .collect(Collectors.toSet());
    }
}
