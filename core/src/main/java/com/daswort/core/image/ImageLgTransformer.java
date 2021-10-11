package com.daswort.core.image;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageLgTransformer implements ImageTransformer {

    @Override
    public BufferedImage transform(BufferedImage image) {
        return image;
    }

    @Override
    public TransformationType imageTransformerType() {
        return TransformationType.LG;
    }
}
