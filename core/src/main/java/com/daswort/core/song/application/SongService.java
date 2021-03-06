package com.daswort.core.song.application;

import com.daswort.core.common.entity.EntitySequenceName;
import com.daswort.core.common.entity.SequenceGenerator;
import com.daswort.core.song.command.SaveSongCommand;
import com.daswort.core.song.converter.SaveCommandUpdateConverter;
import com.daswort.core.song.domain.*;
import com.daswort.core.song.query.SongSearchQuery;
import com.daswort.core.song.repository.SongRepository;
import com.daswort.core.specification.SongSearchSpecification;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final SongFileService songFileService;
    private final SequenceGenerator sequenceGenerator;
    private final SongSearchSpecification songSearchSpecification;

    public Optional<Song> findByCode(String songCode) {
        return songRepository.findSongByCode(songCode);
    }

    public List<Song> findByName(String searchTerm) {
        return songRepository.findAllByQuery(searchTerm, PageRequest.of(0, 5));
    }

    public Page<Song> advancedSearch(SongSearchQuery songSearchQuery, Pageable pageable) {
        requireNonNull(songSearchQuery);
        requireNonNull(pageable);
        final Query query = songSearchSpecification.toCriteriaDefinition(songSearchQuery)
                .map(Query::new)
                .orElse(new Query());
        return songRepository.findAll(query, pageable);
    }

    public Song saveSong(SaveSongCommand command) {
        final var songCode = ofNullable(command.getCode()).orElseGet(() -> sequenceGenerator.nextSequence(EntitySequenceName.song));
        final var updateDefinition = SaveCommandUpdateConverter.convert(songCode, command);
        return songRepository.save(songCode, updateDefinition);
    }


    public void deleteSong(String songCode) {
        songRepository.findSongByCode(songCode)
                .map(Song::getFiles)
                .orElse(Collections.emptyList())
                .forEach(file -> songFileService.deleteSongFileResources(songCode, file));
        songRepository.deleteSongByCode(songCode);
    }

    public boolean isReferencedBy(Author author) {
        if (ObjectId.isValid(author.getId())) {
            return songRepository.isReferencedBy(author);
        } else {
            return false;
        }
    }

    public boolean isReferencedBy(Instrument instrument) {
        if (ObjectId.isValid(instrument.getId())) {
            return songRepository.isReferencedBy(instrument);
        } else {
            return false;
        }
    }

    public boolean isReferencedBy(Topic topic) {
        if (ObjectId.isValid(topic.getId())) {
            return songRepository.isReferencedBy(topic);
        } else {
            return false;
        }
    }

    public boolean isReferencedBy(Vocal vocal) {
        if (ObjectId.isValid(vocal.getId())) {
            return songRepository.isReferencedBy(vocal);
        } else {
            return false;
        }
    }

    public void updateRef(Author author) {
        if (ObjectId.isValid(author.getId())) {
            songRepository.updateRef(author);
        }
    }

    public void updateRef(Instrument instrument) {
        if (ObjectId.isValid(instrument.getId())) {
            songRepository.updateRef(instrument);
        }
    }

    public void updateRef(Vocal vocal) {
        if (ObjectId.isValid(vocal.getId())) {
            songRepository.updateRef(vocal);
        }
    }

    public void updateRef(Topic topic) {
        if (ObjectId.isValid(topic.getId())) {
            songRepository.updateRef(topic);
        }
    }

}
