package com.daswort.core.service.song;

import com.daswort.core.entity.Song;
import com.daswort.core.exception.SongNotFoundException;
import com.daswort.core.repository.AuthorRepository;
import com.daswort.core.repository.SongRepository;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.idname.IdNameService;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import static com.daswort.core.service.IdNameCollection.*;
import static org.springframework.data.mongodb.core.FindAndReplaceOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SongUpdateService {

    private final MongoOperations mongoOperations;
    private final SongRepository songRepository;
    private final SongSearchService songSearchService;
    private final CategoryService categoryService;
    private final IdNameService idNameService;
    private final AuthorRepository authorRepository;

    public SongUpdateService(MongoOperations mongoOperations,
                             SongRepository songRepository,
                             SongSearchService songSearchService,
                             CategoryService categoryService,
                             IdNameService idNameService,
                             AuthorRepository authorRepository) {
        this.mongoOperations = mongoOperations;
        this.songRepository = songRepository;
        this.songSearchService = songSearchService;
        this.categoryService = categoryService;
        this.idNameService = idNameService;
        this.authorRepository = authorRepository;
    }

    public Song updateSong(SongUpdate updatedSong, String songId) {

        final var song = songSearchService.findSongById(songId).orElseThrow(SongNotFoundException::new);

        updatedSong.getName().ifPresent(song::setName);
        updatedSong.getCategoryId().flatMap(categoryService::findById).ifPresent(song::setCategory);
        updatedSong.getTagsIds().map(ids -> idNameService.getAllByIds(tag, ids)).ifPresent(song::setTags);
        updatedSong.getCompositionId().map(id -> idNameService.getById(composition, id)).ifPresent(song::setComposition);
        updatedSong.getPartitionId().map(id -> idNameService.getById(partition, id)).ifPresent(song::setPartition);
        updatedSong.getInstrumentsIds().map(ids -> idNameService.getAllByIds(instrument, ids)).ifPresent(song::setInstruments);
        updatedSong.getDifficultyId().map(id -> idNameService.getById(difficulty, id)).ifPresent(song::setDifficulty);
        updatedSong.getWrittenOn().ifPresent(song::setWrittenOn);
        updatedSong.getTopicsIds().map(ids -> idNameService.getAllByIds(topic, ids)).ifPresent(song::setTopics);
        updatedSong.getMelodyId().flatMap(authorRepository::findById).ifPresent(song::setMelody);
        updatedSong.getArrangementId().flatMap(authorRepository::findById).ifPresent(song::setArrangement);
        updatedSong.getAdaptationId().flatMap(authorRepository::findById).ifPresent(song::setAdaptation);

        return mongoOperations.update(Song.class)
                .matching(query(where("id").is(songId)))
                .replaceWith(song)
                .withOptions(options().upsert().returnNew())
                .as(Song.class)
                .findAndReplaceValue();
    }

    public Song createSong(SongUpdate createSong) {
        final var song = songRepository.save(new Song());
        return updateSong(createSong, song.getId());
    }


}
