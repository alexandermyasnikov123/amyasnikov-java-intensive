package net.dunice.intensive.spring_boot.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public interface ImagesService {
    ByteArrayOutputStream compressIfAbsent(InputStream inputStream, long imageSize);

    String store(ByteArrayOutputStream outputStream);

    String compressIfAbsentAndStore(InputStream inputStream, long imageSize);

    String ensureImagesDirectory();

    String resolveImagePath(String requestUrl);

    long deleteImages(List<String> urls);

    default String compressIfAbsentAndStoreSecure(MultipartFile file, Function<String, String> imageFunction) {
        String localPath = null;
        try {
            localPath = compressIfAbsentAndStore(file.getInputStream(), file.getSize());
            return imageFunction.apply(localPath);
        } catch (Exception e) {
            if (localPath != null) {
                deleteImages(List.of(localPath));
            }
            throw new RuntimeException(e);
        }
    }
}
