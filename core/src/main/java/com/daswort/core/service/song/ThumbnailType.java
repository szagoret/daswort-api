package com.daswort.core.service.song;

import com.daswort.core.image.TransformationType;
import com.daswort.core.pdf.ImageFileFormat;
import lombok.Getter;

@Getter
public enum ThumbnailType {
    SM("sm", TransformationType.SM, ImageFileFormat.jpg),
    LG("lg", TransformationType.LG, ImageFileFormat.jpg);

    private final String prefix;
    private final TransformationType transformationType;
    private final ImageFileFormat imageFileFormat;

    ThumbnailType(String prefix, TransformationType transformationType, ImageFileFormat imageFileFormat) {
        this.prefix = prefix;
        this.transformationType = transformationType;
        this.imageFileFormat = imageFileFormat;
    }
}
