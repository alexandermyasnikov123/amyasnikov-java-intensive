package net.dunice.intensive.spring_boot.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dunice.intensive.spring_boot.services.ImagesService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/images")
@RequiredArgsConstructor
public class ImagesController {
    private final ImagesService imagesService;

    @GetMapping(value = "/{fileName}")
    @SneakyThrows
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        final var url = imagesService.resolveImagePath(fileName);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE)
                .body(new FileSystemResource(url));
    }
}
