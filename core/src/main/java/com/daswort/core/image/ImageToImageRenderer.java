package com.daswort.core.image;


import com.daswort.core.pdf.ImageFileFormat;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageToImageRenderer {

    public ByteArrayOutputStream render(ImageTransformer imageTransformer, InputStream inputStream, ImageFileFormat fileFormat) throws IOException {
        final var bufferedImage = ImageIO.read(inputStream);
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageTransformer.transform(bufferedImage), fileFormat.name(), byteArrayOutputStream);
        return byteArrayOutputStream;
    }
}
