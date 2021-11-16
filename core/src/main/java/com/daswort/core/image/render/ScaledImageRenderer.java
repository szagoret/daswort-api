package com.daswort.core.image.render;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ScaledImageRenderer implements ImageRenderer {

    @Override
    public ByteArrayOutputStream render(ImageRenderCommand cmd) throws IOException {
        final var bufferedImage = ImageIO.read(cmd.getInputStream());
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(cmd.getTransformationType().getImageTransformer().transform(bufferedImage), cmd.getTransformationType().getImageTransformer().getFileFormat().name(), byteArrayOutputStream);
        return byteArrayOutputStream;
    }
}
