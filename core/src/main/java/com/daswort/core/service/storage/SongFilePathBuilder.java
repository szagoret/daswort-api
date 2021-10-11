package com.daswort.core.service.storage;

import org.springframework.stereotype.Component;

import static java.lang.String.join;
import static java.util.Objects.requireNonNull;

@Component
public class SongFilePathBuilder {
    private final String DELIMITER = "/";
    private final String songPathTemplate = join(DELIMITER, "song", "%s");
    private final String songFilePathTemplate = join(DELIMITER,  songPathTemplate, "%s");

    public String build(String songCode) {
        requireNonNull(songCode);
        return String.format(songPathTemplate, songCode);
    }

    public String build(String songCode, String fileCode) {
        requireNonNull(songCode);
        return String.format(songFilePathTemplate, songCode, fileCode);
    }
}
