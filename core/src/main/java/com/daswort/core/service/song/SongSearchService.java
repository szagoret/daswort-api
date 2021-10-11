package com.daswort.core.service.song;

import com.daswort.core.entity.File;
import com.daswort.core.entity.Song;
import com.daswort.core.exception.SongNotFoundException;
import com.daswort.core.model.SongSearch;
import com.daswort.core.model.SongSearchResult;
import com.daswort.core.repository.SongRepository;
import com.daswort.core.specification.SongSearchSpecification;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
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
@RequiredArgsConstructor
public class SongSearchService {

    private final MongoOperations mongoOperations;
    private final SongRepository songRepository;
    private final SongSearchSpecification songSearchSpecification;

    public Optional<Song> findSongByCode(String songCode) {
        return songRepository.findSongByCode(songCode);
    }

    public List<Song> findSongsByName(String searchTerm) {
        return songRepository.findAllByQuery(searchTerm, PageRequest.of(0, 5));
    }

    public File getSongFile(String songCode, String songFileCode) {
        requireNonNull(songCode);
        requireNonNull(songFileCode);

        final var aggregationOperations = List.of(
                new MatchOperation(where("code").is(songCode)),
                new ProjectionOperation(fields("files")),
                new UnwindOperation(field("files")),
                new ReplaceRootOperation(field("files")),
                new MatchOperation(where("code").is(songFileCode))
        );

        return mongoOperations.aggregate(newAggregation(aggregationOperations), Song.class, File.class).getUniqueMappedResult();
    }


//    @Cacheable(value = "songs", key = "#songSearch.name")
    public SongSearchResult advancedSearch(SongSearch songSearch, Pageable pageable) {
        requireNonNull(songSearch);
        requireNonNull(pageable);

        final var sortMap = new HashMap<String, String>();
        sortMap.put("name", "name");
        sortMap.put("arrangement", "arrangement.firstName");
        sortMap.put("melody", "melody.name");
        sortMap.put("adaptation", "adaptation.name");

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
