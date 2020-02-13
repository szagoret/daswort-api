package com.daswort.core.repository;


import com.daswort.core.entity.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SongRepository extends PagingAndSortingRepository<Song, String> {

    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<Song> findAllByQuery(String searchTerm, Pageable pageable);
}
