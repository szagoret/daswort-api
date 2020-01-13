package com.daswort.core.service.song;

import com.daswort.core.entity.File;
import com.daswort.core.entity.Song;
import com.daswort.core.model.SongSearch;
import com.daswort.core.repository.SongRepository;
import com.daswort.core.specification.SongSearchSpecification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.fields;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SongSearchService {

    private final MongoOperations mongoOperations;
    private final SongRepository songRepository;
    private final SongSearchSpecification songSearchSpecification;

    public SongSearchService(MongoOperations mongoOperations,
                             SongRepository songRepository,
                             SongSearchSpecification songSearchSpecification) {
        this.mongoOperations = mongoOperations;
        this.songRepository = songRepository;
        this.songSearchSpecification = songSearchSpecification;
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

    public File getSongFile(String songId, String songFileCode) {
        requireNonNull(songId);
        requireNonNull(songFileCode);

        final var aggregationOperations = List.of(
                new MatchOperation(where("id").is(songId)),
                new ProjectionOperation(fields("files")),
                new UnwindOperation(field("files")),
                new ReplaceRootOperation(field("files")),
                new MatchOperation(where("fileCode").is(songFileCode))
        );

        return mongoOperations.aggregate(newAggregation(aggregationOperations), Song.class, File.class).getUniqueMappedResult();
    }


    public List<Song> advancedSearch(SongSearch songSearch, Pageable pageable) {
        final Query query = new Query(songSearchSpecification.toCriteriaDefinition(songSearch))
                .with(pageable);
        return mongoOperations.find(query, Song.class);
    }
}
