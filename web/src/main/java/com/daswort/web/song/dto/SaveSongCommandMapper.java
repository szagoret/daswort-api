package com.daswort.web.song.dto;

import com.daswort.core.common.model.IdTitleEntity;
import com.daswort.core.song.command.SaveSongCommand;
import com.daswort.core.song.domain.Author;
import com.daswort.core.song.domain.Instrument;
import com.daswort.core.song.domain.Topic;
import com.daswort.core.song.domain.Vocal;
import com.daswort.web.common.IdTitleDto;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SaveSongCommandMapper {

    public static SaveSongCommand toSaveSongCommand(SongDto songDto) {
        return SaveSongCommand.builder()
                .code(songDto.getCode())
                .title(songDto.getTitle())
                .originalTitle(songDto.getOriginalTitle())
                .language(songDto.getLanguage())
                .instruments(toIdTitleEntities(songDto.getInstruments(), Instrument.class))
                .vocals(toIdTitleEntities(songDto.getVocals(), Vocal.class))
                .topics(toIdTitleEntities(songDto.getTopics(), Topic.class))
                .composers(toAuthor(songDto.getComposers()))
                .arrangers(toAuthor(songDto.getArrangers()))
                .orchestrators(toAuthor(songDto.getOrchestrators()))
                .translators(toAuthor(songDto.getTranslators()))
                .build();
    }

    @SneakyThrows
    private static <T extends IdTitleEntity> T toIdTitleEntity(IdTitleDto dto, Class<T> clazz) {
        return clazz.getDeclaredConstructor(String.class, String.class).newInstance(dto.getId(), dto.getTitle());
    }

    @SneakyThrows
    private static <T extends IdTitleEntity> List<T> toIdTitleEntities(List<IdTitleDto> dtos, Class<T> clazz) {
        if(dtos == null) {
            return List.of();
        }
        return dtos.stream().map(dto -> toIdTitleEntity(dto, clazz)).collect(Collectors.toList());
    }

    private static Author toAuthor(AuthorDto dto) {
        return new Author(dto.getId(), dto.getFirstName(), dto.getLastName());
    }

    private static List<Author> toAuthor(List<AuthorDto> dtos) {
        return Optional.ofNullable(dtos).orElse(List.of())
                .stream()
                .map(SaveSongCommandMapper::toAuthor)
                .collect(Collectors.toList());
    }
}
