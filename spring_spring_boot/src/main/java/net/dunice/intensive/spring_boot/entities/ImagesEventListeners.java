package net.dunice.intensive.spring_boot.entities;

import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.spring_boot.services.ImagesService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;

@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class ImagesEventListeners {
    private final ImagesService imagesService;

    @PostRemove
    public void deleteImageFromDisc(QuizImageEntity quizImage) {
        imagesService.deleteImages(List.of(quizImage.getUrl()));
    }
}
