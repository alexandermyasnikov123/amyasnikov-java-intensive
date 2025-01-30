package net.dunice.intensive.spring_boot.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.spring_boot.dtos.requests.QuizRequest;
import net.dunice.intensive.spring_boot.dtos.responses.PageResponse;
import net.dunice.intensive.spring_boot.services.QuizzesService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/quizzes")
@RequiredArgsConstructor
public class QuizzesController {
    private final QuizzesService quizzesService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Object> createQuiz(
            @Valid
            @RequestBody
            QuizRequest request,
            @RequestParam(required = false)
            List<MultipartFile> images,
            HttpServletRequest servletRequest
    ) {
        final var response = quizzesService.createQuiz(request, images, servletRequest);
        return ResponseEntity.created(URI.create(response.location())).build();
    }

    @GetMapping
    public ResponseEntity<Object> findQuizzes(
            @Min(value = PageResponse.MIN_PAGE_NUMBER)
            @RequestParam
            Integer page,
            @Range(min = PageResponse.MIN_PAGE_SIZE, max = PageResponse.MAX_PAGE_SIZE)
            @RequestParam
            Integer pageSize
    ) {
        final var response = quizzesService.findAll(page, pageSize);
        return ResponseEntity.ok(response);
    }
}
