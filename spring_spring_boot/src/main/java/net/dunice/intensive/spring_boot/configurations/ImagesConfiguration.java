package net.dunice.intensive.spring_boot.configurations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "files.images")
@Getter
@RequiredArgsConstructor(onConstructor_ = @ConstructorBinding)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImagesConfiguration {
    long maxSizeBytes;

    double outputQuality;

    String outputFormat;

    String outputPrefix;

    String outputExtension;

    String subdirectory;
}
