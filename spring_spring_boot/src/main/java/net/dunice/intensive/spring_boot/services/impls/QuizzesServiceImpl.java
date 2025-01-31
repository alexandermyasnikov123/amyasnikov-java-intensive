package net.dunice.intensive.spring_boot.services.impls;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.spring_boot.dtos.requests.QuizRequest;
import net.dunice.intensive.spring_boot.dtos.responses.CreationResponse;
import net.dunice.intensive.spring_boot.dtos.responses.DeletionResponse;
import net.dunice.intensive.spring_boot.dtos.responses.PageResponse;
import net.dunice.intensive.spring_boot.dtos.responses.QuizResponse;
import net.dunice.intensive.spring_boot.entities.QuizEntity;
import net.dunice.intensive.spring_boot.mappers.QuizzesMapper;
import net.dunice.intensive.spring_boot.repositories.QuizzesRepository;
import net.dunice.intensive.spring_boot.services.ImagesService;
import net.dunice.intensive.spring_boot.services.QuizzesService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizzesServiceImpl implements QuizzesService {
    private static final String CACHE_BY_ID_NAME = "quiz-by-id";

    private static final String CACHE_BY_PAGE_AND_SIZE = "quizzes";

    private final ImagesService imagesService;

    private final QuizzesRepository quizzesRepository;

    private final QuizzesMapper quizzesMapper;

    @Override
    @Transactional
    @CachePut(value = CACHE_BY_ID_NAME, key = "#result.data().id()")
    public CreationResponse<QuizResponse> createQuiz(
            QuizRequest request,
            @Nullable List<MultipartFile> images
    ) {

        final var imageUrls = images == null ? List.<String>of() : images.stream().map(file ->
                imagesService.compressIfAbsentAndStoreSecure(
                        file,
                        localPath -> ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .replacePath("api/v1")
                                .path("/images")
                                .path("/" + Paths.get(localPath).getFileName().toString())
                                .toUriString()
                )
        ).toList();

        final var quiz = quizzesRepository.save(quizzesMapper.mapToEntity(request, new HashSet<>(imageUrls)));
        final var response = quizzesMapper.mapToResponse(quiz);

        return new CreationResponse<>(response, ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/" + response.id())
                .encode()
                .build()
                .toUri()
        );
    }

    @Override
    @Caching(
            cacheable = @Cacheable(
                    value = CACHE_BY_PAGE_AND_SIZE,
                    key = "#page + '_' + #pageSize",
                    condition = "#result != null && not(#result.data().empty)"
            ),
            evict = @CacheEvict(
                    value = CACHE_BY_PAGE_AND_SIZE,
                    key = "#page + '_' + #pageSize",
                    condition = "#result == null || #result.data().empty"
            )
    )
    public PageResponse<QuizResponse> findAll(
            int page,
            int pageSize
    ) {
        final var pageData = quizzesRepository.findAll(PageRequest.of(page - 1, pageSize));
        final var quizzes = pageData.map(quizzesMapper::mapToResponse).toList();

        return new PageResponse<>(quizzes, (long) pageData.getNumberOfElements(), pageData.getTotalElements());
    }

    @Override
    @Cacheable(value = CACHE_BY_ID_NAME, key = "#id")
    public QuizResponse findById(Long id) {
        return quizzesRepository.findById(id)
                .map(quizzesMapper::mapToResponse)
                .orElseThrow();
    }

    @Override
    @Transactional
    @CacheEvict(
            value = CACHE_BY_ID_NAME,
            key = "#result.deletedIds().first",
            condition = "not(#result.deletedIds().empty)"
    )
    public DeletionResponse<Long> deleteById(Long id) {
        final var isDeleted = quizzesRepository.deleteAllByIdIn(List.of(id)) > 0;
        return new DeletionResponse<>(List.of(isDeleted ? id : -1L));
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_BY_PAGE_AND_SIZE, CACHE_BY_ID_NAME}, allEntries = true)
    public DeletionResponse<Long> deleteAll() {
        final var ids = quizzesRepository.findAll()
                .stream()
                .map(QuizEntity::getId)
                .toList();

        quizzesRepository.deleteAll();
        return new DeletionResponse<>(ids);
    }
}
