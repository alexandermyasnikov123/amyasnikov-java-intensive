package net.dunice.intensive.spring_boot.mappers;

import net.dunice.intensive.spring_boot.dtos.shared.QuizDto;
import net.dunice.intensive.spring_boot.entities.QuizEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class QuizzesMapper {
    @Autowired
    protected ImagesMapper imagesMapper;

    @Autowired
    protected AnswersMapper answersMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answers", expression = "java(answersMapper.mapToEntities(dto.rightAnswer(), dto.others()))")
    @Mapping(target = "images", expression = "java(imagesMapper.mapToEntities(dto.images(), quizEntity))")
    public abstract QuizEntity mapToEntity(QuizDto dto);

    @Mapping(target = "rightAnswer", expression = "java(answersMapper.mapRightAnswer(entity.getAnswers()))")
    @Mapping(target = "others", expression = "java(answersMapper.mapOthers(entity.getAnswers()))")
    @Mapping(target = "images", expression = "java(imagesMapper.mapToUrls(entity.getImages()))")
    public abstract QuizDto mapToDto(QuizEntity entity);
}
