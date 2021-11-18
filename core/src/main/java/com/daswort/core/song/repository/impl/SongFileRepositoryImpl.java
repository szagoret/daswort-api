package com.daswort.core.song.repository.impl;

import com.daswort.core.song.domain.Song;
import com.daswort.core.song.domain.SongFile;
import com.daswort.core.song.repository.SongFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.fields;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@RequiredArgsConstructor
public class SongFileRepositoryImpl implements SongFileRepository {
    private final MongoOperations mongoOperations;

    @Override
    public Optional<SongFile> getSongFile(String songCode, String songFileCode) {
        requireNonNull(songCode);
        requireNonNull(songFileCode);

        final var aggregationOperations = List.of(
                new MatchOperation(where("code").is(songCode)),
                new ProjectionOperation(fields("files")),
                new UnwindOperation(field("files")),
                new ReplaceRootOperation(field("files")),
                new MatchOperation(where("code").is(songFileCode))
        );

        return Optional.ofNullable(mongoOperations.aggregate(newAggregation(aggregationOperations), Song.class, SongFile.class).getUniqueMappedResult());
    }

    @Override
    public List<SongFile> getSongFiles(String songCode) {
        final var aggregationOperations = List.of(
                new MatchOperation(where("code").is(songCode)),
                new ProjectionOperation(fields("files")),
                new UnwindOperation(field("files")),
                new ReplaceRootOperation(field("files")));
        return mongoOperations.aggregate(newAggregation(aggregationOperations), Song.class, SongFile.class).getMappedResults();
    }

    @Override
    public Optional<SongFile> saveSongFile(String songCode, SongFile songFile) {
        final var query = new Query(where("code").is(songCode));
        final var update = new Update().push("files", songFile);
        final var updatedSong = mongoOperations.findAndModify(query, update, options().returnNew(true), Song.class);
        return updatedSong == null ? Optional.empty() : Optional.of(songFile);
    }

    @Override
    public void makeFilePrimary(String songCode, String fileCode, boolean isPrimary) {
        mongoOperations.updateFirst(query(where("code").is(songCode)), new Update().set("files.$[].primary", false), Song.class);
        mongoOperations.updateFirst(query(where("code").is(songCode).and("files.code").is(fileCode)), new Update().set("files.$.primary", isPrimary), Song.class);
    }

    @Override
    public void deleteSongFile(String songCode, String fileCode) {
        final var query = new Query(where("code").is(songCode));
        final var update = new Update().pull("files", new Query(where("code").is(fileCode)).getQueryObject());
        mongoOperations.updateFirst(query, update, Song.class);
    }
}
