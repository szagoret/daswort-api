package com.daswort.core.song.application;

import com.daswort.core.image.render.ImageRenderCommand;
import com.daswort.core.image.render.ImageRendererFactory;
import com.daswort.core.image.transform.ImageTransformationType;
import com.daswort.core.storage.FileResource;
import com.daswort.core.storage.FileResourceBytes;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
public class SongFileThumbnailService {

    @SneakyThrows
    public FileResource createFileThumbnail(SongFileResource songFileResource, ImageTransformationType imageTransformationType) {
        final var fileCode = songFileResource.getEntity().getCode();
        final var renderedCmd = ImageRenderCommand.builder()
                .inputStream(new ByteArrayInputStream(songFileResource.getBytes()))
                .extension(songFileResource.getEntity().getExtension())
                .transformationType(imageTransformationType)
                .build();

        final var byteArrayOutputStream = ImageRendererFactory.resolve(songFileResource.getEntity().getExtension()).render(renderedCmd);
        return new FileResourceBytes(byteArrayOutputStream.toByteArray(), fileCode, "application/octet-stream");
    }
}
