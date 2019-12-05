package com.daswort.core.repository;

import com.daswort.core.entity.Song;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SongRepository extends PagingAndSortingRepository<Song, String> {
}
