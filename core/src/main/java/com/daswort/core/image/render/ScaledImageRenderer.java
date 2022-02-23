package com.daswort.core.image.render;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ScaledImageRenderer implements ImageRenderer {

    @Override
    public List<ByteArrayOutputStream> render(ImageRenderCommand cmd) throws IOException {
        final var bufferedImage = ImageIO.read(cmd.getInputStream());
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(cmd.getTransformationType().getImageTransformer().transform(bufferedImage), cmd.getTransformationType().getImageTransformer().getFileFormat().name(), byteArrayOutputStream);
        return List.of(byteArrayOutputStream);
    }
}
