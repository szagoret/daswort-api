package com.daswort.core.service.song;

import com.daswort.core.image.ImageTransformerFactory;
import com.daswort.core.pdf.PdfToImageRenderer;
import com.daswort.core.storage.FileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SongThumbnailService {
    private final ImageTransformerFactory imageTransformerFactory;

    public ByteArrayOutputStream createSongThumbnail(FileResource fileResource,
                                                     Integer pageIndex,
                                                     ThumbnailType thumbnailType) {
        try {
            return PdfToImageRenderer.builder()
                    .pdfInputStream(fileResource.getInputStream())
                    .page(pageIndex)
                    .fileFormat(thumbnailType.getImageFileFormat())
                    .build()
                    .render(imageTransformerFactory.get(thumbnailType.getTransformationType()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
