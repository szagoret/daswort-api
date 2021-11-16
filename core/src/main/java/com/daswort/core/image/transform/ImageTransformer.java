package com.daswort.core.image.transform;

import com.daswort.core.image.ImageFileFormat;

import java.awt.image.BufferedImage;

public interface ImageTransformer {
    BufferedImage transform(BufferedImage image);

    default ImageFileFormat getFileFormat() {
        return ImageFileFormat.jpg;
    }
}
