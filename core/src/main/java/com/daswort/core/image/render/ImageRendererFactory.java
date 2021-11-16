package com.daswort.core.image.render;

import java.util.List;

public class ImageRendererFactory {

    public static ImageRenderer resolve(String fileExtension) {
        final var isImage = List.of("jpg", "jpeg", "png").contains(fileExtension);
        if ("pdf".equals(fileExtension)) {
            return new PdfToImageRenderer();
        } else if (isImage) {
            return new ScaledImageRenderer();
        } else {
            throw new RuntimeException("unsupported file type");
        }
    }
}
