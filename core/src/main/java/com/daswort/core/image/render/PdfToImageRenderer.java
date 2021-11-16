package com.daswort.core.image.render;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfToImageRenderer implements ImageRenderer {
    @Override
    public ByteArrayOutputStream render(ImageRenderCommand cmd) throws IOException {
        final var pdDocument = PDDocument.load(cmd.getInputStream());
        final var bufferedImage = new PDFRenderer(pdDocument).renderImage(cmd.getPageIndex());
        final var imageTransformer = cmd.getTransformationType().getImageTransformer();
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIOUtil.writeImage(imageTransformer.transform(bufferedImage), imageTransformer.getFileFormat().name(), byteArrayOutputStream);
        pdDocument.close();
        return byteArrayOutputStream;
    }
}
