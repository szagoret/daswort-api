package com.daswort.core.song.repository.impl;

import com.daswort.core.song.domain.Song;
import com.daswort.core.song.repository.SongCustomRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@AllArgsConstructor
public class SongCustomRepositoryImpl implements SongCustomRepository {
    private final MongoOperations mongoOperations;

    @Override
    public Song save(String songCode, UpdateDefinition updateDefinition) {
        return mongoOperations.update(Song.class)
                .matching(query(where("code").is(songCode)))
                .apply(updateDefinition)
                .withOptions(FindAndModifyOptions.options().upsert(true).returnNew(true))
                .findAndModifyValue();
    }

    @Override
    public Page<Song> findAll(Query query, Pageable pageable) {
        final long count = mongoOperations.count(query, Song.class);
        final List<Song> songList = mongoOperations.find(query.with(pageable), Song.class);
        return new PageImpl<>(songList, pageable, count);
    }
}
