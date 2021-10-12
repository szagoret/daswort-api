package com.daswort.web.song;

import com.daswort.core.entity.Song;

import java.util.List;
import java.util.Objects;

import static com.daswort.web.author.AuthorDtoMapper.toAuthorDto;
import static com.daswort.web.idname.IdNameDtoMapper.toIdNameDto;
import static java.util.Optional.ofNullable;

public class SongDtoMapper {

    public static SongDto toSongDto(Song song) {
        Objects.requireNonNull(song);
        return SongDto.builder()
                .code(song.getCode())
                .name(song.getName())
                .composition(toIdNameDto(song.getComposition()))
                .difficulty(toIdNameDto(song.getDifficulty()))
                .topics(toIdNameDto(song.getTopics()))
                .arrangement(toIdNameDto(song.getArrangement()))
                .melody(toIdNameDto(song.getMelody()))
                .adaptation(toIdNameDto(song.getAdaptation()))
                .difficulty(toIdNameDto(song.getDifficulty()))
                .instruments(toIdNameDto(song.getInstruments()))
                .createdAt(song.getCreatedAt())
                .files(ofNullable(song.getFiles()).orElse(List.of()))
                .build();
    }
}
