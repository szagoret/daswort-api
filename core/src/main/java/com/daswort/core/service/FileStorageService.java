package com.daswort.core.service;

import com.daswort.core.entity.Song;
import com.daswort.core.repository.CategoryRepository;
import com.daswort.core.repository.SongRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileStorageService {

    private final SongRepository songRepository;
    private final CategoryRepository categoryRepository;
    private final MongoOperations mongoOperations;

    public FileStorageService(SongRepository songRepository, CategoryRepository categoryRepository, MongoOperations mongoOperations) {
        this.songRepository = songRepository;
        this.categoryRepository = categoryRepository;
        this.mongoOperations = mongoOperations;
    }

    @Transactional
    public Song getSong() {
        return songRepository.findById("5de72c5039674311328cb734").orElse(Song.builder().build());
    }
}
