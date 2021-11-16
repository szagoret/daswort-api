package com.daswort.web.song.dto;

import com.daswort.core.common.model.IdTitleEntity;
import com.daswort.core.song.domain.Author;
import com.daswort.core.song.domain.Song;
import com.daswort.web.common.IdTitleDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SongDtoMapper {

    public static Optional<SongDto> toSongDto(Song songEntity) {
        return Optional.ofNullable(songEntity).map(song ->
                SongDto.builder()
                        .code(song.getCode())
                        .title(song.getTitle())
                        .originalTitle(song.getOriginalTitle())
                        .instruments(idTitleDtos(song.getInstruments()))
                        .vocals(idTitleDtos(song.getVocals()))
                        .topics(idTitleDtos(song.getTopics()))
                        .composers(authorDtos(song.getComposers()))
                        .arrangers(authorDtos(song.getArrangers()))
                        .orchestrators(authorDtos(song.getOrchestrators()))
                        .translators(authorDtos(song.getTranslators()))
                        .publishDate(song.getPublishDate())
                        .language(song.getLanguage())
                        .songFiles(song.getSongFiles())
                        .build());
    }

    private static IdTitleDto idTitleDto(IdTitleEntity entity) {
        return Optional.ofNullable(entity).map(e -> IdTitleDto.builder()
                        .id(e.getId())
                        .title(e.getTitle())
                        .build())
                .orElse(null);
    }

    private static List<IdTitleDto> idTitleDtos(List<? extends IdTitleEntity> entities) {
        return Optional.ofNullable(entities).orElse(List.of()).stream()
                .map(SongDtoMapper::idTitleDto)
                .collect(Collectors.toList());
    }

    private static AuthorDto authorDto(Author entity) {
        return Optional.ofNullable(entity).map(e -> AuthorDto.builder()
                        .id(e.getId())
                        .firstName(e.getFirstName())
                        .lastName(e.getLastName())
                        .build())
                .orElse(null);
    }

    private static List<AuthorDto> authorDtos(List<Author> entities) {
        return Optional.ofNullable(entities).orElse(List.of()).stream()
                .map(SongDtoMapper::authorDto)
                .collect(Collectors.toList());
    }
}
