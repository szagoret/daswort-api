package com.daswort.core.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfToImageRenderer {

    public ByteArrayOutputStream render(InputStream pdfInputStream) throws IOException {
        final var document = PDDocument.load(pdfInputStream);
        final var bufferedImage = new PDFRenderer(document).renderImage(0);
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIOUtil.writeImage(bufferedImage, ImageFileFormat.png.name(), byteArrayOutputStream);
        return byteArrayOutputStream;
    }
}
