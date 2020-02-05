package com.daswort.core.service.song;

import com.daswort.core.entity.File;
import com.daswort.core.entity.IdName;
import com.daswort.core.entity.IdNameCollection;
import com.daswort.core.entity.Song;
import com.daswort.core.exception.SongNotFoundException;
import com.daswort.core.model.SongUpdate;
import com.daswort.core.repository.AuthorRepository;
import com.daswort.core.repository.SongRepository;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.idname.IdNameService;
import com.daswort.core.storage.FileResource;
import com.daswort.core.utils.FileUtils;
import com.daswort.core.utils.IdNameSongUtils;
import com.mongodb.QueryBuilder;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.daswort.core.entity.IdNameCollection.*;
import static java.lang.String.join;
import static java.util.Objects.requireNonNull;
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
    private final SongFileService songFileService;

    public SongUpdateService(MongoOperations mongoOperations,
                             SongRepository songRepository,
                             SongSearchService songSearchService,
                             CategoryService categoryService,
                             IdNameService idNameService,
                             AuthorRepository authorRepository, SongFileService songFileService) {
        this.mongoOperations = mongoOperations;
        this.songRepository = songRepository;
        this.songSearchService = songSearchService;
        this.categoryService = categoryService;
        this.idNameService = idNameService;
        this.authorRepository = authorRepository;
        this.songFileService = songFileService;
    }

    public Song updateSong(SongUpdate updatedSong, String songId) {
        requireNonNull(updatedSong);
        requireNonNull(songId);

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
        updatedSong.getArrangementId().flatMap(authorRepository::findById).ifPresent(song::setArrangement);

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


    public void removeSong(String songId) {
        final var files = songRepository.findById(songId).map(Song::getFiles).orElse(Collections.emptyList());
        files.forEach(file -> songFileService.removeSongFile(songId, file.getFileCode()));
        songRepository.deleteById(songId);
    }

    public File addSongFile(String songId, FileResource fileResource) {
        requireNonNull(songId);
        requireNonNull(fileResource);
        final var foundSong = songRepository.findById(songId).orElseThrow(SongNotFoundException::new);
        final var fileCode = songFileService.saveSongFile(foundSong.getId(), fileResource);
        final var file = File.builder()
                .name(fileResource.getName())
                .fileCode(fileCode)
                .extension(FileUtils.getFileExtension(fileResource.getName()))
                .size(fileResource.getContentLength())
                .build();

        final var query = new Query(where("id").is(foundSong.getId()));
        final var update = new Update().push("files", file);

        mongoOperations.findAndModify(query, update, Song.class);
        return file;
    }

    public void removeSongFile(String songId, String fileCode) {
        final var removeElement = new Update().pull("files", QueryBuilder.start("fileCode").is(fileCode).get());
        mongoOperations.update(Song.class)
                .matching(query(where("id").is(songId)))
                .apply(removeElement).first();

        songFileService.removeSongFile(songId, fileCode);
    }

    public void removeSongIdNameField(String fieldName) {
        requireNonNull(fieldName);
        final var updateOperation = new Update().unset(fieldName);
        mongoOperations.update(Song.class).apply(updateOperation).all();
    }

    public void removeSongIdNameArrayItem(String fieldName, String itemId) {
        requireNonNull(fieldName, itemId);
        final var removeElementOperation = new Update().pull(fieldName, QueryBuilder.start("id").is(itemId).get());
        mongoOperations.update(Song.class).apply(removeElementOperation).all();
    }

    public void updateSongIdNameField(String fieldName, IdName updateObject) {
        requireNonNull(fieldName);
        requireNonNull(updateObject);
        final var update = new Update().set(fieldName, updateObject);
        mongoOperations.update(Song.class)
                .apply(update)
                .all();
    }

    public void updateSongIdNameArrayItem(String fieldName, IdName updateObject) {
        requireNonNull(fieldName);
        requireNonNull(updateObject);

        final var update = new Update().set(join(".", fieldName, "$"), updateObject);
        mongoOperations.updateMulti(query(where(join(".", fieldName, "_id")).is(updateObject.getId())), update, Song.class);
    }

    public void updateSongField(IdNameCollection collection, IdName updateObject) {
        requireNonNull(collection);
        requireNonNull(updateObject);
        String fieldName = IdNameSongUtils.getFieldName(collection).orElseThrow();
        Class<?> fieldType = IdNameSongUtils.getFieldType(collection).orElseThrow();
        if (fieldType.isAssignableFrom(List.class)) {
            updateSongIdNameArrayItem(fieldName, updateObject);
        } else if (fieldType.isAssignableFrom(IdName.class)) {
            updateSongIdNameField(fieldName, updateObject);
        } else {
            throw new IllegalArgumentException();
        }

    }

    public void removeSongField(IdNameCollection collection, String fieldItemId) {
        requireNonNull(collection);
        requireNonNull(fieldItemId);
        String fieldName = IdNameSongUtils.getFieldName(collection).orElseThrow();
        Class<?> fieldType = IdNameSongUtils.getFieldType(collection).orElseThrow();
        if (fieldType.isAssignableFrom(List.class)) {
            removeSongIdNameArrayItem(fieldName, fieldItemId);
        } else if (fieldType.isAssignableFrom(IdName.class)) {
            removeSongIdNameField(fieldName);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
