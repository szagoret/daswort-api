package com.daswort.core.service.song;

import com.daswort.core.entity.File;
import com.daswort.core.entity.Song;
import com.daswort.core.exception.SongNotFoundException;
import com.daswort.core.model.SongSearch;
import com.daswort.core.model.SongSearchResult;
import com.daswort.core.repository.SongRepository;
import com.daswort.core.specification.SongSearchSpecification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        if (!ObjectId.isValid(songId)) {
            throw new SongNotFoundException();
        }
        return songRepository.findById(songId);
    }

    public List<Song> findSongsByName(String searchTerm) {
        return songRepository.findAllByQuery(searchTerm, PageRequest.of(0, 5));
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


    public SongSearchResult advancedSearch(SongSearch songSearch, Pageable pageable) {
        requireNonNull(songSearch);
        requireNonNull(pageable);

        final var sortMap = new HashMap<String, String>();
        sortMap.put("name", "name");
        sortMap.put("arrangement", "arrangement.firstName");
        sortMap.put("composition", "composition.name");
        sortMap.put("difficulty", "difficulty.name");
        sortMap.put("createdAt", "createdAt");

        String sortDirection = songSearch.getSortDirection();
        String sortProperty = songSearch.getSortProperty();
        final var sortBy = Sort.by(Sort.Direction.valueOf(sortDirection), sortMap.get(sortProperty));
        final Query query = songSearchSpecification.toCriteriaDefinition(songSearch)
                .map(Query::new)
                .orElse(new Query());
        Long count = mongoOperations.count(query, Song.class);
        List<Song> songList = mongoOperations.find(query.with(pageable).with(sortBy), Song.class);
        return SongSearchResult.builder()
                .songList(songList)
                .totalCount(count)
                .pageable(pageable)
                .sortDirection(sortDirection)
                .sortProperty(sortProperty)
                .build();
    }
}
