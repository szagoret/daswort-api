package com.daswort.core.song.repository;


import com.daswort.core.song.domain.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends PagingAndSortingRepository<Song, String>, SongCustomRepository {

    @Query("{$text:{$search:?0}}")
    List<Song> findAllByQuery(String searchTerm, Pageable pageable);

    @Query("{'code': ?0}")
    Optional<Song> findSongByCode(String code);

    @DeleteQuery("{'code': ?0}")
    void deleteSongByCode(String code);
}
