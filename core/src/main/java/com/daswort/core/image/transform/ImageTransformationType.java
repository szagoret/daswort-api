package com.daswort.core.image.transform;

import lombok.Getter;

public enum ImageTransformationType {
    SM(new ImageSmTransformer()),
    LG(new ImageLgTransformer());

    @Getter
    private final ImageTransformer imageTransformer;

    ImageTransformationType(ImageTransformer imageTransformer) {
        this.imageTransformer = imageTransformer;
    }
}
