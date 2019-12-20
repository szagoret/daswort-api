package com.daswort.core.service.storage;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiFunction;

import static java.lang.String.join;
import static java.util.Objects.requireNonNull;

@Component
public class SongFileLocationResolver implements BiFunction<String, String, String> {
    private final String DELIMITER = "/";
    private final String songFilesPathTemplate = join(DELIMITER, "song", "%s", "files", "%s");

    public static String generateFileCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String apply(String songId, String fileCode) {
        requireNonNull(fileCode);
        requireNonNull(songId);
        return String.format(songFilesPathTemplate, songId, fileCode);
    }
}
