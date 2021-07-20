package com.daswort.core.service.storage;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.TriFunction;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiFunction;

import static java.lang.String.join;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class SongFileThumbnailLocationResolver implements TriFunction<String, String, String, String> {
    private final SongFileLocationResolver songFileLocationResolver;
    private final String DELIMITER = "/";
    private final String thumbnailPathTemplate = join(DELIMITER, "%s", "thumbnails", "%s");

    @Override
    public String apply(String songId, String parentFileCode, String thumbnailCode) {
        requireNonNull(parentFileCode);
        requireNonNull(songId);
        final var parentSongPath = songFileLocationResolver.apply(songId, parentFileCode);
        return String.format(thumbnailPathTemplate, parentSongPath, thumbnailCode);
    }
}
