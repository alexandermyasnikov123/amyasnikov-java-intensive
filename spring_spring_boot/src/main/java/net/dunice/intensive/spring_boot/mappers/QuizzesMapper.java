package net.dunice.intensive.spring_boot.mappers;

import net.dunice.intensive.spring_boot.dtos.requests.QuizRequest;
import net.dunice.intensive.spring_boot.dtos.responses.QuizResponse;
import net.dunice.intensive.spring_boot.entities.QuizEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class QuizzesMapper {
    @Autowired
    protected ImagesMapper imagesMapper;

    @Autowired
    protected AnswersMapper answersMapper;

    @Mapping(target = "rightAnswer", expression = "java(answersMapper.mapRightAnswer(entity.getAnswers()))")
    @Mapping(target = "others", expression = "java(answersMapper.mapOthers(entity.getAnswers()))")
    @Mapping(target = "images", expression = "java(imagesMapper.mapToUrls(entity.getImages()))")
    public abstract QuizResponse mapToDto(QuizEntity entity);

    @Mapping(target = "images", expression = "java(imagesMapper.mapToEntities(images, quizEntity))")
    @Mapping(
            target = "answers",
            expression = "java(answersMapper.mapToEntities(request.rightAnswer(), request.others()))"
    )
    @Mapping(target = "id", ignore = true)
    public abstract QuizEntity mapToEntity(QuizRequest request, Set<String> images);
}
