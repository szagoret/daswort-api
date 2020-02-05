package com.daswort.web.mapper;

import com.daswort.core.entity.Song;
import com.daswort.web.dto.song.SongDto;

import java.util.Objects;

import static com.daswort.web.mapper.AuthorDtoMapper.toAuthorDto;
import static com.daswort.web.mapper.IdNameDtoMapper.toIdNameDto;

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
                .difficulty(toIdNameDto(song.getDifficulty()))
                .build();
    }
}
