package com.daswort.core.service.song;

import com.daswort.core.entity.Song;
import com.daswort.core.repository.SongRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Song> findSongsByName(String searchTerm) {
        TextCriteria fullTextSearch = TextCriteria.forLanguage("de").matchingAny(searchTerm);
        TextQuery query = TextQuery.queryText(fullTextSearch);
        query.sortByScore();
        query.limit(10);
        return mongoOperations.find(query, Song.class);
    }

}
