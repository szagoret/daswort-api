package com.daswort.core.service.song;

import com.daswort.core.entity.*;
import com.daswort.core.exception.SongNotFoundException;
import com.daswort.core.model.SongUpdate;
import com.daswort.core.repository.AuthorRepository;
import com.daswort.core.repository.SongRepository;
import com.daswort.core.service.idname.IdNameService;
import com.daswort.core.storage.FileResource;
import com.daswort.core.utils.FileUtils;
import com.daswort.core.utils.IdNameSongUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.daswort.core.entity.IdNameCollection.composition;
import static com.daswort.core.entity.IdNameCollection.topic;
import static java.lang.String.join;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.springframework.data.mongodb.core.FindAndReplaceOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class SongUpdateService {

    private final MongoOperations mongoOperations;
    private final SongRepository songRepository;
    private final SongSearchService songSearchService;
    private final IdNameService idNameService;
    private final AuthorRepository authorRepository;
    private final SongFileService songFileService;

    public Song updateSong(SongUpdate updatedSong, String songId) {
        requireNonNull(updatedSong);
        requireNonNull(songId);

        final var song = songSearchService.findSongById(songId).orElseThrow(SongNotFoundException::new);
        song.setName(updatedSong.getName());

        updatedSong.getComposition().map(IdName::getId).map(id -> idNameService.getById(composition, id)).ifPresent(song::setComposition);
        updatedSong.getTopics().map(idNames -> idNames.stream().map(IdName::getId).collect(toSet()))
                .map(ids -> idNameService.getAllByIds(topic, ids)).ifPresent(song::setTopics);
        updatedSong.getArrangement().map(Author::getId).flatMap(authorRepository::findById).ifPresent(song::setArrangement);
        updatedSong.getAdaptation().map(Author::getId).flatMap(authorRepository::findById).ifPresent(song::setAdaptation);
        updatedSong.getMelody().map(Author::getId).flatMap(authorRepository::findById).ifPresent(song::setMelody);

        return mongoOperations.update(Song.class)
                .matching(query(where("id").is(songId)))
                .replaceWith(song)
                .withOptions(options().upsert().returnNew())
                .as(Song.class)
                .findAndReplaceValue();
    }

    public Song createSong(final SongUpdate createSong) {
        final var songToCreate = new Song();
        songToCreate.setCreatedAt(Instant.now());
        final var song = songRepository.save(songToCreate);
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

    public Song removeSongFile(String songId, String fileCode) {
        final var query = query(where("id").is(songId));
        final var update = new Update().pull("files", new Document().append("fileCode", fileCode));
//        songFileService.removeSongFile(songId, fileCode);
        return mongoOperations.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Song.class);
    }

    public void removeSongIdNameField(String fieldName) {
        requireNonNull(fieldName);
        final var updateOperation = new Update().unset(fieldName);
        mongoOperations.update(Song.class).apply(updateOperation).all();
    }

    public void removeSongIdNameArrayItem(String fieldName, String itemId) {
        requireNonNull(fieldName, itemId);
//        final var removeElementOperation = new Update().pull(fieldName, QueryBuilder.start("id").is(itemId).get());
//        mongoOperations.update(Song.class).apply(removeElementOperation).all();
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
