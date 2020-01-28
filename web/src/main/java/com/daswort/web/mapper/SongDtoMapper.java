package com.daswort.web.mapper;

import com.daswort.core.entity.Song;
import com.daswort.web.dto.song.SongDto;

import java.util.Objects;

public class SongDtoMapper {

    public static SongDto toSongDto(Song song) {
        Objects.requireNonNull(song);
        return SongDto.builder()
                .id(song.getId())
                .name(song.getName())
                .build();
    }
}
