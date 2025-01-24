package net.dunice.intensive.spring_boot.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dunice.intensive.spring_boot.dtos.requests.QuizRequest;
import net.dunice.intensive.spring_boot.services.QuizzesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createQuiz(
            @Valid
            @RequestBody
            QuizRequest request,
            @RequestParam(required = false)
            List<MultipartFile> images
    ) {

        final var response = quizzesService.createQuiz(request, images);
        return ResponseEntity
                .created(URI.create(response.location()))
                .build();
    }
}
