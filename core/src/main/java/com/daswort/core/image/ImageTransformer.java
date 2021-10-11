package com.daswort.core.image;

import java.awt.image.BufferedImage;

public interface ImageTransformer {
    BufferedImage transform(BufferedImage image);

    TransformationType imageTransformerType();
}
