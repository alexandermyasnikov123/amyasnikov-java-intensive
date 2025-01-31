package net.dunice.intensive.spring_boot.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.spring_boot.dtos.requests.QuizRequest;
import net.dunice.intensive.spring_boot.dtos.responses.PageResponse;
import net.dunice.intensive.spring_boot.services.QuizzesService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping(value = "/quizzes")
@RequiredArgsConstructor
public class QuizzesController {
    private final QuizzesService quizzesService;

    @PostMapping
    public ResponseEntity<?> createQuiz(
            @Valid
            @RequestPart
            QuizRequest quiz,
            @RequestPart(required = false)
            List<MultipartFile> images,
            HttpServletRequest servletRequest
    ) {
        final var response = quizzesService.createQuiz(quiz, images, servletRequest);
        return ResponseEntity.created(response.location()).build();
    }

    @GetMapping
    public ResponseEntity<?> findQuizzes(
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findQuizById(
            @Positive
            @PathVariable
            Long id
    ) {
        final var response = quizzesService.findById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteQuizById(
            @Positive
            @PathVariable
            Long id
    ) {
        final var response = quizzesService.deleteById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllQuizzes() {
        final var response = quizzesService.deleteAll();
        return ResponseEntity.ok(response);
    }
}
