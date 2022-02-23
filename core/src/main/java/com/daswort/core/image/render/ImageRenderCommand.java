package com.daswort.core.image.render;

import com.daswort.core.image.transform.ImageTransformationType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class ImageRenderCommand {
    @NonNull
    InputStream inputStream;
    @NonNull
    String extension;
    @NonNull
    ImageTransformationType transformationType;
}
