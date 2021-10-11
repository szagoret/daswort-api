package com.daswort.core.pdf;

import com.daswort.core.image.ImageTransformer;
import lombok.Builder;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfToImageRenderer {
    @NonNull
    PDDocument pdDocument;
    @NonNull
    Integer page;
    @NonNull
    ImageFileFormat fileFormat;

    @Builder
    public PdfToImageRenderer(InputStream pdfInputStream, Integer page, ImageFileFormat fileFormat) throws IOException {
        this.pdDocument = PDDocument.load(pdfInputStream);
        this.page = page;
        this.fileFormat = fileFormat;
    }

    public ByteArrayOutputStream render(ImageTransformer imageTransformer) throws IOException {
        final var bufferedImage = new PDFRenderer(pdDocument).renderImage(this.page);
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIOUtil.writeImage(imageTransformer.transform(bufferedImage), fileFormat.name(), byteArrayOutputStream);
        pdDocument.close();
        return byteArrayOutputStream;
    }
}
