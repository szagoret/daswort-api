package com.daswort.core.song.application;

import com.daswort.core.common.entity.EntitySequenceName;
import com.daswort.core.common.entity.SequenceGenerator;
import com.daswort.core.song.command.SaveSongCommand;
import com.daswort.core.song.converter.SaveCommandUpdateConverter;
import com.daswort.core.song.domain.Author;
import com.daswort.core.song.domain.Song;
import com.daswort.core.song.query.SongSearchQuery;
import com.daswort.core.song.repository.SongRepository;
import com.daswort.core.specification.SongSearchSpecification;
import lombok.RequiredArgsConstructor;
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

    public void updateAuthors(Author author) {
        songRepository.updateAuthor(author);
    }

}
