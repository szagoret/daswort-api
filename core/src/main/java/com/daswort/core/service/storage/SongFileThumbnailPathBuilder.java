package com.daswort.core.service.storage;

import com.daswort.core.service.song.ThumbnailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.String.join;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class SongFileThumbnailPathBuilder {
    private final SongFilePathBuilder songFilePathBuilder;
    private final String DELIMITER = "/";
    private final String thumbnailPathTemplate = join(DELIMITER, "%s", "thumbnails", "%s", "%s-%d.%s");

    public String build(String songCode,
                        String parentFileCode,
                        ThumbnailType thumbnailType,
                        Integer page) {
        requireNonNull(parentFileCode);
        requireNonNull(songCode);
        final var parentSongPath = songFilePathBuilder.build(songCode);
        return String.format(thumbnailPathTemplate, parentSongPath, thumbnailType.getPrefix(), parentFileCode, page, thumbnailType.getImageFileFormat().name());
    }
}
