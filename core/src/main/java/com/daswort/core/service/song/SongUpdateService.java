package com.daswort.core.service.song;

import com.daswort.core.EntitySequenceName;
import com.daswort.core.SequenceGenerator;
import com.daswort.core.entity.File;
import com.daswort.core.entity.IdName;
import com.daswort.core.entity.IdNameCollection;
import com.daswort.core.entity.Song;
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
import java.util.Optional;
import java.util.Set;

import static com.daswort.core.entity.IdNameCollection.composition;
import static com.daswort.core.entity.IdNameCollection.topic;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
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
    private final SequenceGenerator sequenceGenerator;

    public Song updateSong(SongUpdate updatedSong, String songCode) {
        requireNonNull(updatedSong);
        requireNonNull(songCode);

        final var song = songSearchService.findSongByCode(songCode).orElseThrow(SongNotFoundException::new);
        song.setName(updatedSong.getName());

        updatedSong.getComposition().map(IdName::getId).map(id -> idNameService.getById(composition, id)).ifPresent(song::setComposition);
        updatedSong.getTopics().map(idNames -> idNames.stream().map(IdName::getId).collect(toSet()))
                .map(ids -> idNameService.getAllByIds(topic, ids)).ifPresent(song::setTopics);
        final Optional<IdName> optArrangement = updatedSong.getArrangement();
        if (optArrangement.isPresent()) {
            optArrangement.map(IdName::getId).flatMap(authorRepository::findById).ifPresent(song::setArrangement);
        } else {
            song.setArrangement(null);
        }

        final Optional<IdName> optAdaptation = updatedSong.getAdaptation();
        if (optAdaptation.isPresent()) {
            optAdaptation.map(IdName::getId).flatMap(authorRepository::findById).ifPresent(song::setAdaptation);
        } else {
            song.setAdaptation(null);
        }

        final Optional<IdName> optMelody = updatedSong.getMelody();
        if (optMelody.isPresent()) {
            optAdaptation.map(IdName::getId).flatMap(authorRepository::findById).ifPresent(song::setMelody);
        } else {
            song.setMelody(null);
        }


        return mongoOperations.update(Song.class)
                .matching(query(where("code").is(songCode)))
                .replaceWith(song)
                .withOptions(options().upsert().returnNew())
                .as(Song.class)
                .findAndReplaceValue();
    }

    public void makeFilePrimary(String songCode, String fileCode, boolean isPrimary) {
        mongoOperations.updateFirst(query(where("code").is(songCode)), new Update().set("files.$[].primary", false), Song.class);
        mongoOperations.updateFirst(query(where("code").is(songCode).and("files.code").is(fileCode)), new Update().set("files.$.primary", isPrimary), Song.class);
    }

    public Song createSong(final SongUpdate createSong) {
        final var newSong = new Song();
        newSong.setCode(sequenceGenerator.nextSequence(EntitySequenceName.song));
        newSong.setCreatedAt(Instant.now());
        final var song = songRepository.save(newSong);
        return updateSong(createSong, song.getCode());
    }


    public void removeSong(String songCode) {
        final var files = songRepository.findSongByCode(songCode).map(Song::getFiles).orElse(Collections.emptyList());
        files.forEach(file -> {
            songFileService.removeSongFile(songCode, file.getCode());
            ofNullable(file.getLgThumbnails()).orElse(Set.of()).forEach(songFileService::removeSongFile);
            ofNullable(file.getSmThumbnails()).orElse(Set.of()).forEach(songFileService::removeSongFile);
        });
        songRepository.deleteSongByCode(songCode);
    }

    public File addSongFile(String songCode, FileResource fileResource) {
        requireNonNull(songCode);
        requireNonNull(fileResource);
        final var foundSong = songRepository.findSongByCode(songCode).orElseThrow(SongNotFoundException::new);
        final var fileExtension = FileUtils.getFileExtension(fileResource.getName());
        final var sequence = sequenceGenerator.nextSequence(EntitySequenceName.file);
        final var filePath = songFileService.saveSongFile(foundSong.getCode(), fileResource, sequence, fileExtension.orElse(""));
        final var file = File.builder()
                .name(fileResource.getName())
                .code(format("%s%s", sequence, fileExtension.map(s -> "." + s).orElse("")))
                .path(filePath)
                .extension(fileExtension.orElse(""))
                .size(fileResource.getContentLength())
                .smThumbnails(Set.of())
                .lgThumbnails(Set.of())
                .uploadedAt(Instant.now())
                .build();

        final var query = new Query(where("id").is(foundSong.getId()));
        final var update = new Update().push("files", file);

        mongoOperations.findAndModify(query, update, Song.class);
        return file;
    }

    public void createSongFileThumbnails(String songCode, String fileCode) {
        songFileService.createFileThumbnail(songCode, fileCode);
    }

    public Song removeSongFile(String songCode, String fileCode) {
        final var query = query(where("code").is(songCode));
        final var update = new Update().pull("files", new Document().append("code", fileCode));

        songSearchService.getSongFile(songCode, fileCode).ifPresent(file -> {
            songFileService.removeSongFile(songCode, fileCode);
            ofNullable(file.getLgThumbnails()).orElse(Set.of()).forEach(songFileService::removeSongFile);
            ofNullable(file.getSmThumbnails()).orElse(Set.of()).forEach(songFileService::removeSongFile);
        });


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
