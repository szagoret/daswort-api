package com.daswort.web.author;

import com.daswort.core.song.domain.Author;
import com.daswort.web.common.IdTitleDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class AuthorIdNameDtoMapper {
    public static IdTitleDto toIdNameDto(Author author) {
        return Optional.ofNullable(author).map(a ->
                IdTitleDto.builder()
                        .id(a.getId())
                        .title(String.join(" ", a.getFirstName(), a.getLastName()))
                        .build()
        ).orElse(IdTitleDto.builder().build());
    }

    public static List<IdTitleDto> toIdNameDto(List<Author> authorList) {
        return ofNullable(authorList)
                .map(authors -> authors.stream()
                        .map(AuthorIdNameDtoMapper::toIdNameDto).collect(Collectors.toList()))
                .orElse(List.of());
    }
}
