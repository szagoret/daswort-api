package com.daswort.core.service.song;

import com.daswort.core.repository.SongRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class SongSearchService {

    private final MongoOperations mongoOperations;
    private final SongRepository songRepository;

    public SongSearchService(MongoOperations mongoOperations,
                             SongRepository songRepository) {
        this.mongoOperations = mongoOperations;
        this.songRepository = songRepository;
    }



}
