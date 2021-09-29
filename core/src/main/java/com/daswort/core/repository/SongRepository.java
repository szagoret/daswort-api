package com.daswort.core.repository;


import com.daswort.core.entity.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends PagingAndSortingRepository<Song, String> {

    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<Song> findAllByQuery(String searchTerm, Pageable pageable);

    @Query("{'code': ?0}")
    Optional<Song> findSongByCode(String code);

    @DeleteQuery("{'code': ?0}")
    void deleteSongByCode(String code);
}
