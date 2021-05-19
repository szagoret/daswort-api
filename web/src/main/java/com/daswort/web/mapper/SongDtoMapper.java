package com.daswort.web.mapper;

import com.daswort.core.entity.Song;
import com.daswort.web.dto.song.SongDto;

import java.util.List;
import java.util.Objects;

import static com.daswort.web.mapper.AuthorDtoMapper.toAuthorDto;
import static com.daswort.web.mapper.IdNameDtoMapper.toIdNameDto;
import static java.util.Optional.ofNullable;

public class SongDtoMapper {

    public static SongDto toSongDto(Song song) {
        Objects.requireNonNull(song);
        return SongDto.builder()
                .id(song.getId())
                .name(song.getName())
                .composition(toIdNameDto(song.getComposition()))
                .difficulty(toIdNameDto(song.getDifficulty()))
                .topics(toIdNameDto(song.getTopics()))
                .arrangement(toAuthorDto(song.getArrangement()))
                .melody(toAuthorDto(song.getMelody()))
                .adaptation(toAuthorDto(song.getAdaptation()))
                .difficulty(toIdNameDto(song.getDifficulty()))
                .instruments(toIdNameDto(song.getInstruments()))
                .createdAt(song.getCreatedAt())
                .files(ofNullable(song.getFiles()).orElse(List.of()))
                .build();
    }
}
