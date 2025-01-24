package net.dunice.intensive.spring_boot.services.impls;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.spring_boot.dtos.requests.QuizRequest;
import net.dunice.intensive.spring_boot.dtos.responses.CreationResponse;
import net.dunice.intensive.spring_boot.dtos.responses.DeletionResponse;
import net.dunice.intensive.spring_boot.dtos.responses.PageResponse;
import net.dunice.intensive.spring_boot.dtos.responses.QuizResponse;
import net.dunice.intensive.spring_boot.mappers.QuizzesMapper;
import net.dunice.intensive.spring_boot.repositories.QuizzesRepository;
import net.dunice.intensive.spring_boot.services.ImagesService;
import net.dunice.intensive.spring_boot.services.QuizzesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizzesServiceImpl implements QuizzesService {
    private static final String MAPPING = "/quizzes/";

    private final ImagesService imagesService;

    private final QuizzesRepository quizzesRepository;

    private final QuizzesMapper quizzesMapper;

    @Value("${server.servlet.context-path}")
    private final String servicePath;

    @Override
    public CreationResponse<Long> createQuiz(QuizRequest request, @Nullable List<MultipartFile> images) {
        List<String> imageUrls = null;
        try {

            imageUrls = images != null ? images.stream().map(file -> {
                try {
                    return imagesService.compressIfAbsentAndStore(file.getInputStream(), file.getSize());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toList() : List.of();

            final var id = quizzesRepository.save(
                    quizzesMapper.mapToEntity(request, new HashSet<>(imageUrls))
            ).getId();

            return new CreationResponse<>(id, servicePath + MAPPING + id);
        } catch (Exception e) {
            imagesService.deleteImages(imageUrls);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<QuizResponse> findAll(int page, int pageSize) {
        final var pageData = quizzesRepository.findAll(PageRequest.of(page - 1, pageSize));
        final var quizzes = pageData.map(quizzesMapper::mapToDto).toList();

        return new PageResponse<>(quizzes, (long) pageData.getNumberOfElements(), pageData.getTotalElements());
    }

    @Override
    public QuizResponse findById(Long id) {
        return quizzesRepository.findById(id)
                .map(quizzesMapper::mapToDto)
                .orElseThrow();
    }

    @Override
    public DeletionResponse<Long> deleteById(Long... ids) {
        final var idsList = Arrays.asList(ids);

        return new DeletionResponse<>(quizzesRepository.deleteAllByIdIn(idsList));
    }
}
