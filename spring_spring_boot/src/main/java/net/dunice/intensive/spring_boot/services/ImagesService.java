package net.dunice.intensive.spring_boot.services;

import lombok.SneakyThrows;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public interface ImagesService {
    @SneakyThrows
    ByteArrayOutputStream compressIfAbsent(InputStream inputStream, long imageSize);

    @SneakyThrows
    String store(ByteArrayOutputStream outputStream);

    String compressIfAbsentAndStore(InputStream inputStream, long imageSize);

    String ensureImagesDirectory();

    long deleteImages(List<String> urls);
}
