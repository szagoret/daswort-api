package com.daswort.core.service.song;

import com.daswort.core.entity.Song;
import com.daswort.core.repository.SongRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongSearchService {

    private final MongoOperations mongoOperations;
    private final SongRepository songRepository;

    public SongSearchService(MongoOperations mongoOperations,
                             SongRepository songRepository) {
        this.mongoOperations = mongoOperations;
        this.songRepository = songRepository;
    }

    public Optional<Song> findSongById(String songId) {
        return songRepository.findById(songId);
    }


}
