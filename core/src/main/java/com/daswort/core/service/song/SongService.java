package com.daswort.core.service.song;

import com.daswort.core.entity.Song;
import com.daswort.core.service.EntityUpdateService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongService {

    private final EntityUpdateService<Song> songUpdateService;
    private final SongSearchService songSearchService;

    public SongService(EntityUpdateService<Song> songUpdateService, SongSearchService songSearchService) {
        this.songUpdateService = songUpdateService;
        this.songSearchService = songSearchService;
    }

    public Optional<Song> findSongById(String songId) {
        return songSearchService.findSongById(songId);
    }
}
