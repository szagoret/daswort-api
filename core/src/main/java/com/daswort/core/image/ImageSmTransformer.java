package com.daswort.core.image;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

@Component
public class ImageSmTransformer implements ImageTransformer {

    @Override
    public BufferedImage transform(BufferedImage image) {
        final Image scaledImage = image.getScaledInstance(Math.min(300, image.getWidth()), -1, 4);
        final var newBufferedImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D bGr = newBufferedImage.createGraphics();
        bGr.drawImage(scaledImage, 0, 0, null);
        bGr.dispose();

        return newBufferedImage.getSubimage(0,0, newBufferedImage.getWidth(), newBufferedImage.getHeight() / 2);
    }

    @Override
    public TransformationType imageTransformerType() {
        return TransformationType.SM;
    }
}
