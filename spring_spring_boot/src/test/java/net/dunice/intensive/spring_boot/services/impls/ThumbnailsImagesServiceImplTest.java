package net.dunice.intensive.spring_boot.services.impls;

import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import net.dunice.intensive.spring_boot.configurations.ImagesConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Stack;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ThumbnailsImagesServiceImplTest {
    private ImagesConfiguration configuration;

    private ThumbnailsImagesServiceImpl thumbnailsImagesService;

    @BeforeEach
    public void setUp() {
        configuration = mock();
        thumbnailsImagesService = new ThumbnailsImagesServiceImpl(configuration);
    }

    @Test
    @SneakyThrows
    public void compressIfAbsentOnlyCompressesWhenSizeIsGreaterThan_configuration_getMaxSizeBytes() {
        try (var context = mockStatic(Thumbnails.class)) {
            final var maxBytes = 10L;
            final var initialBytes = 20L;
            final var expectedScale = 0.5;

            final var contextBuilder = mock(Thumbnails.Builder.class);

            context.when(() -> Thumbnails.of(any(InputStream.class))).thenReturn(contextBuilder);
            when(contextBuilder.scale(anyDouble())).thenReturn(contextBuilder);
            when(contextBuilder.outputQuality(anyDouble())).thenReturn(contextBuilder);
            doNothing().when(contextBuilder).toOutputStream(any());

            when(configuration.getMaxSizeBytes()).thenReturn(maxBytes);
            when(configuration.getOutputQuality()).thenReturn(1.0);

            final var inputStream = mock(InputStream.class);
            thumbnailsImagesService.compressIfAbsent(inputStream, initialBytes);

            verify(inputStream, never()).transferTo(any(OutputStream.class));
            verify(contextBuilder).scale(expectedScale);
        }
    }

    @Test
    public void compressIfAbsentDoesntCompressesWhenSizeIsLessOrEqualTo_configuration_getMaxSizeBytes() {
        LongStream.of(5L, 10L).forEach(initialBytes -> {

            try (var context = mockStatic(Thumbnails.class)) {
                final var maxBytes = 10L;
                final var expectedScale = 0.5;

                final var contextBuilder = mock(Thumbnails.Builder.class);

                context.when(() -> Thumbnails.of(any(InputStream.class))).thenReturn(contextBuilder);
                when(contextBuilder.scale(anyDouble())).thenReturn(contextBuilder);
                when(contextBuilder.outputQuality(anyDouble())).thenReturn(contextBuilder);
                doNothing().when(contextBuilder).toOutputStream(any());

                when(configuration.getMaxSizeBytes()).thenReturn(maxBytes);
                when(configuration.getOutputQuality()).thenReturn(1.0);

                final var inputStream = mock(InputStream.class);
                thumbnailsImagesService.compressIfAbsent(inputStream, initialBytes);

                context.verify(() -> Thumbnails.of(any(InputStream.class)), never());
                verify(inputStream).transferTo(any(OutputStream.class));
                verify(contextBuilder, never()).scale(expectedScale);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SneakyThrows
    @Test
    public void storeEnsuresDirectoryIsCreatedAndCreatesRightFileNameWhenSaving() {
        final var imagesDir = "/images/dir/";
        final var imagePrefix = "prefix_";
        final var imageDateFormat = "dddd";
        final var imageExtension = ".ext";

        final var expectedDate = "sunday";
        final var expectedFileName = imagesDir + imagePrefix + expectedDate + imageExtension;

        try (
                var ignored1 = mockConstruction(SimpleDateFormat.class, (mock, context) -> {
                    when(mock.format(any())).thenReturn(expectedDate);
                });
                var ignored2 = mockConstruction(FileOutputStream.class)
        ) {
            final var mockByteStream = mock(ByteArrayOutputStream.class);
            doNothing().when(mockByteStream).writeTo(any());

            final var serviceSpy = spy(thumbnailsImagesService);
            when(serviceSpy.ensureImagesDirectory()).thenReturn(imagesDir);
            when(configuration.getOutputPrefix()).thenReturn(imagePrefix);
            when(configuration.getOutputFormat()).thenReturn(imageDateFormat);
            when(configuration.getOutputExtension()).thenReturn(imageExtension);

            final var actualName = serviceSpy.store(mockByteStream);

            final var spyOrder = inOrder(serviceSpy);
            spyOrder.verify(serviceSpy).ensureImagesDirectory();

            assertEquals(expectedFileName, actualName);
        }
    }

    @Test
    public void compressIfAbsentAndStore() {
        final var serviceMock = mock(ThumbnailsImagesServiceImpl.class);

        final var byteArrayStream = mock(ByteArrayOutputStream.class);
        final var expectedPath = "expected/path";

        when(serviceMock.compressIfAbsent(any(), anyLong())).thenReturn(byteArrayStream);
        when(serviceMock.store(byteArrayStream)).thenReturn(expectedPath);
        when(serviceMock.compressIfAbsentAndStore(any(), anyLong())).thenCallRealMethod();

        final var actualPath = serviceMock.compressIfAbsentAndStore(mock(), 1);

        final var inOrder = inOrder(serviceMock);
        inOrder.verify(serviceMock).compressIfAbsent(any(), anyLong());
        inOrder.verify(serviceMock).store(byteArrayStream);

        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void ensureImagesDirectoryCreatesDirectoryIfNotExistsYet() {
        try (
                var pathsContext = mockStatic(Paths.class);
                var fileContext = mockConstruction(File.class, (mock, context) -> {
                    when(mock.mkdirs()).thenReturn(true);
                })
        ) {
            final var expectedPath = "expected/path";
            final var mockPath = mock(Path.class);

            pathsContext.when(() -> Paths.get("")).thenReturn(mockPath);
            when(mockPath.toAbsolutePath()).thenReturn(mockPath);
            when(mockPath.toString()).thenReturn("expected");

            when(configuration.getSubdirectory()).thenReturn("/path");

            final var actualPath = thumbnailsImagesService.ensureImagesDirectory();

            final var mockFile = fileContext.constructed().getLast();
            verify(mockFile).mkdirs();
            assertEquals(expectedPath, actualPath);
        }
    }

    @Test
    @SneakyThrows
    void deleteImagesReturnsOnlyDeletedImages() {
        final var expectedSize = 2;
        final var urls = List.of("url1", "url2", "url3");
        final var willBeDeleted = new Stack<String>();

        willBeDeleted.addAll(urls.stream().limit(expectedSize).toList());


        try (var ignored = mockStatic(Files.class)) {
            ignored.when(() -> Files.deleteIfExists(any()))
                    .then(invocation -> !willBeDeleted.isEmpty() && !willBeDeleted.pop().isBlank());

            final var actualSize = thumbnailsImagesService.deleteImages(urls);
            assertEquals(expectedSize, actualSize);
        }
    }
}