package net.dunice.intensive.spring_boot.services.impls;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import net.dunice.intensive.spring_boot.configurations.ImagesConfiguration;
import net.dunice.intensive.spring_boot.services.ImagesService;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThumbnailsImagesServiceImpl implements ImagesService {
    private final ImagesConfiguration configuration;

    @SneakyThrows
    @Override
    public ByteArrayOutputStream compressIfAbsent(InputStream inputStream, long imageSize) {
        final var outputStream = new ByteArrayOutputStream();

        if (imageSize <= configuration.getMaxSizeBytes()) {
            inputStream.transferTo(outputStream);
        } else {
            final var scaleFactor = 1.0 / ((double) imageSize / configuration.getMaxSizeBytes());
            Thumbnails.of(inputStream)
                    .scale(scaleFactor)
                    .outputQuality(configuration.getOutputQuality())
                    .toOutputStream(outputStream);
        }
        return outputStream;
    }

    @SneakyThrows
    @Override
    public String store(ByteArrayOutputStream outputStream) {
        final var imagePath = ensureImagesDirectory() +
                configuration.getOutputPrefix() +
                new SimpleDateFormat(configuration.getOutputFormat()).format(new Date()) +
                configuration.getOutputExtension();

        try (var fileOutputStream = new FileOutputStream(imagePath)) {
            outputStream.writeTo(fileOutputStream);
        }

        return imagePath;
    }

    @Override
    public String compressIfAbsentAndStore(InputStream inputStream, long imageSize) {
        final var compressed = compressIfAbsent(inputStream, imageSize);
        return store(compressed);
    }

    @Override
    public String ensureImagesDirectory() {
        final var finalPath = Paths.get("").toAbsolutePath() + configuration.getSubdirectory();
        new File(finalPath).mkdirs();
        return finalPath;
    }

    @Override
    public long deleteImages(List<String> urls) {
        return urls.stream()
                .filter(url -> {
                    try {
                        return Files.deleteIfExists(Path.of(url));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).count();
    }
}
