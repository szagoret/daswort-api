package com.daswort.core.image.render;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface ImageRenderer {
    List<ByteArrayOutputStream> render(ImageRenderCommand cmd) throws IOException;
}
