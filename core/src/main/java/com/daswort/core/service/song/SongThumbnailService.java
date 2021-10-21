package com.daswort.core.service.song;

import com.daswort.core.image.ImageToImageRenderer;
import com.daswort.core.image.ImageTransformerFactory;
import com.daswort.core.pdf.PdfToImageRenderer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongThumbnailService {
    private final ImageTransformerFactory imageTransformerFactory;

    public ByteArrayOutputStream createSongThumbnail(SongFileResource songFileResource,
                                                     Integer pageIndex,
                                                     ThumbnailType thumbnailType) {
        final var fileExtension = songFileResource.getFile().getExtension();
        final var isImage = List.of("jpg", "jpeg", "png").contains(fileExtension.toLowerCase());
        final var imageTransformer = imageTransformerFactory.get(thumbnailType.getTransformationType());
        final var inputStream = songFileResource.getFileResource().getInputStream();
        try {
            if ("pdf".equals(songFileResource.getFile().getExtension())) {
                return PdfToImageRenderer.builder()
                        .pdfInputStream(inputStream)
                        .page(pageIndex)
                        .fileFormat(thumbnailType.getImageFileFormat())
                        .build()
                        .render(imageTransformer);
            } else if (isImage) {
                return new ImageToImageRenderer().render(imageTransformer, inputStream, thumbnailType.getImageFileFormat());
            } else {
                throw new RuntimeException("unsupported file type");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
