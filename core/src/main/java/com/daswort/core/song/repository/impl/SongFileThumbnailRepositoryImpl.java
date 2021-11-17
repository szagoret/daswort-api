package com.daswort.core.song.repository.impl;

import com.daswort.core.song.domain.Song;
import com.daswort.core.song.domain.Thumbnail;
import com.daswort.core.song.repository.SongFileThumbnailRepository;
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

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.fields;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@RequiredArgsConstructor
public class SongFileThumbnailRepositoryImpl implements SongFileThumbnailRepository {
    private final MongoOperations mongoOperations;

    @Override
    public Optional<Thumbnail> findByCode(String songCode, String fileCode, String thumbCode) {

        final var aggregationOperations = List.of(
                new MatchOperation(where("code").is(songCode)),
                new ProjectionOperation(fields("files")),
                new UnwindOperation(field("files")),
                new ReplaceRootOperation(field("files")),
                new MatchOperation(where("code").is(fileCode)),
                new ProjectionOperation(fields("thumbnails")),
                new UnwindOperation(field("thumbnails")),
                new ReplaceRootOperation(field("thumbnails")),
                new MatchOperation(where("code").is(thumbCode))
        );

        return Optional.ofNullable(mongoOperations.aggregate(newAggregation(aggregationOperations), Song.class, Thumbnail.class).getUniqueMappedResult());

    }

    @Override
    public Optional<Thumbnail> save(String songCode, String fileCode, Thumbnail thumbnail) {
        final var query = new Query(where("code").is(songCode));
        final var update = new Update()
                .push("files.$[e].thumbnails", thumbnail)
                .filterArray("e.code", fileCode);

        final var updatedSong = mongoOperations.findAndModify(query, update, options().returnNew(true), Song.class);
        return updatedSong == null ? Optional.empty() : Optional.of(thumbnail);
    }

    @Override
    public void deleteThumbnails(String songCode, String fileCode, List<String> thumbCodes) {
        final var query = new Query(where("code").is(songCode).and("files.code").is(fileCode));
        final var update = new Update()
                .pull("files.$.thumbnails", new Query(where("code").in(thumbCodes)).getQueryObject());
        mongoOperations.updateFirst(query, update, Song.class);
    }

    @Override
    public void deleteAllThumbnails(String songCode, String fileCode) {
        final var query = new Query(where("code").is(songCode).and("files.code").is(fileCode));
        final var update = new Update()
                .pull("files.$.thumbnails", new Query().getQueryObject());
        mongoOperations.updateFirst(query, update, Song.class);
    }
}
