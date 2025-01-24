package net.dunice.intensive.spring_boot.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "files.images")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class ImagesConfiguration {
    long maxSizeBytes;

    double outputQuality;

    String outputFormat;

    String outputPrefix;

    String outputExtension;

    String subdirectory;
}
