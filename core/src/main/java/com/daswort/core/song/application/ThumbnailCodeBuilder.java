package com.daswort.core.song.application;

import com.daswort.core.image.ImageFileFormat;

public final class ThumbnailCodeBuilder {
    public static String build(String fileCode, String type, Integer page, ImageFileFormat imageFileFormat) {
        return String.format("%s-%s-%d.%s", fileCode, type, page, imageFileFormat.name().toLowerCase());
    }
}
