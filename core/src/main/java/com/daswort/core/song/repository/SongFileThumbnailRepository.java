package com.daswort.core.song.repository;

import com.daswort.core.song.domain.Thumbnail;

import java.util.Optional;

public interface SongFileThumbnailRepository {

    Optional<Thumbnail> save(String songCode, String fileCode, Thumbnail thumbnail);

    Optional<Thumbnail> findByCode(String songCode, String fileCode, String thumbCode);
}
