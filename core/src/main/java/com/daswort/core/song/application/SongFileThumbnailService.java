package com.daswort.core.song.application;

import com.daswort.core.image.render.ImageRenderCommand;
import com.daswort.core.image.render.ImageRendererFactory;
import com.daswort.core.image.transform.ImageTransformationType;
import com.daswort.core.song.domain.SongFileResource;
import com.daswort.core.storage.FileResource;
import com.daswort.core.storage.FileResourceBytes;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SongFileThumbnailService {

    @SneakyThrows
    public List<FileResource> createFileThumbnails(SongFileResource songFileResource, ImageTransformationType imageTransformationType) {
        final var fileCode = songFileResource.getEntity().getCode();
        final var renderedCmd = ImageRenderCommand.builder()
                .inputStream(new ByteArrayInputStream(songFileResource.getBytes()))
                .extension(songFileResource.getEntity().getExtension())
                .transformationType(imageTransformationType)
                .build();


        return ImageRendererFactory.resolve(songFileResource.getEntity().getExtension()).render(renderedCmd).stream()
                .map(ByteArrayOutputStream::toByteArray)
                .map(bytes -> new FileResourceBytes(bytes, fileCode, "application/octet-stream"))
                .collect(Collectors.toList());
    }
}
