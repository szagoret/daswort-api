package com.daswort.core.image;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageSmTransformer implements ImageTransformer {

    @Override
    public BufferedImage transform(BufferedImage image) {
        return image.getSubimage(1,1,150,100);
    }

    @Override
    public TransformationType imageTransformerType() {
        return TransformationType.SM;
    }
}
