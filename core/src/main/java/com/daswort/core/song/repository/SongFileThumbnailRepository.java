package com.daswort.core.song.repository;

import com.daswort.core.song.domain.Thumbnail;

import java.util.List;
import java.util.Optional;

public interface SongFileThumbnailRepository {

    Optional<Thumbnail> save(String songCode, String fileCode, Thumbnail thumbnail);

    Optional<Thumbnail> findByCode(String songCode, String fileCode, String thumbCode);

    void deleteThumbnails(String songCode, String fileCode, List<String> thumbCodes);

    void deleteAllThumbnails(String songCode, String fileCode);

    default void deleteThumbnail(String songCode, String fileCode, String thumbCode) {
        deleteThumbnails(songCode, fileCode, List.of(thumbCode));
    }
}
