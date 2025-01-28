package net.dunice.intensive.spring_boot.services;

import jakarta.annotation.Nullable;
import net.dunice.intensive.spring_boot.dtos.requests.QuizRequest;
import net.dunice.intensive.spring_boot.dtos.responses.CreationResponse;
import net.dunice.intensive.spring_boot.dtos.responses.DeletionResponse;
import net.dunice.intensive.spring_boot.dtos.responses.PageResponse;
import net.dunice.intensive.spring_boot.dtos.responses.QuizResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface QuizzesService {

    CreationResponse<QuizResponse> createQuiz(QuizRequest request, @Nullable List<MultipartFile> images);

    PageResponse<QuizResponse> findAll(int page, int pageSize);

    QuizResponse findById(Long id);

    DeletionResponse<Long> deleteById(Long id);

    @CacheEvict
    DeletionResponse<Long> deleteAll();
}
