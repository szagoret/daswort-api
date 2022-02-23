package com.daswort.core.image.render;

import com.daswort.core.image.transform.ImageTransformer;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PdfToImageRenderer implements ImageRenderer {
    @Override
    public List<ByteArrayOutputStream> render(ImageRenderCommand cmd) throws IOException {
        final var response = new ArrayList<ByteArrayOutputStream>();
        try (final var pdDocument = PDDocument.load(cmd.getInputStream())) {
            IntStream.range(0, pdDocument.getNumberOfPages()).mapToObj(page ->
                    render(pdDocument, page, cmd.getTransformationType().getImageTransformer())
            ).forEach(response::add);
        }
        return response;
    }

    @SneakyThrows
    private ByteArrayOutputStream render(PDDocument pdDocument, int page, ImageTransformer imageTransformer) {
        final var bufferedImage = new PDFRenderer(pdDocument).renderImage(page);
        final var byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIOUtil.writeImage(imageTransformer.transform(bufferedImage), imageTransformer.getFileFormat().name(), byteArrayOutputStream);
        return byteArrayOutputStream;
    }
}
