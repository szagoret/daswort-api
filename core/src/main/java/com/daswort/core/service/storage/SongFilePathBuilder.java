package com.daswort.core.service.storage;

import static java.lang.String.join;

public final class SongFilePathBuilder {
    private static final String DELIMITER = "/";
    private static final String songPathTemplate = join(DELIMITER, "song", "%s");
    private static final String songFilePathTemplate = join(DELIMITER, songPathTemplate, "%s");
    private static final String songFileThumbnailPathTemplate = join(DELIMITER, songFilePathTemplate, "%s");

    public static String build(String songCode) {
        return String.format(songPathTemplate, songCode);
    }

    public static String build(String songCode, String fileCode) {
        return String.format(songFilePathTemplate, songCode, fileCode);
    }

    public static String build(String songCode, String fileCode, String thumbnailCode) {
        return String.format(songFileThumbnailPathTemplate, songCode, fileCode, thumbnailCode);
    }
}
