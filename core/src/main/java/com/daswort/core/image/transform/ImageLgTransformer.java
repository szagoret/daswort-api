package com.daswort.core.image.transform;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

@Component
public class ImageLgTransformer implements ImageTransformer {

    @Override
    public BufferedImage transform(BufferedImage image) {
        final Image initialImage = image.getScaledInstance(image.getWidth(), image.getHeight(), 4);
        final var newBufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D bGr = newBufferedImage.createGraphics();
        bGr.drawImage(initialImage, 0, 0, null);
        bGr.dispose();

        return newBufferedImage.getSubimage(0, 0, newBufferedImage.getWidth(), newBufferedImage.getHeight());
    }
}
