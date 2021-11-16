package com.daswort.core.image.render;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ImageRenderer {
    ByteArrayOutputStream render(ImageRenderCommand cmd) throws IOException;
}
