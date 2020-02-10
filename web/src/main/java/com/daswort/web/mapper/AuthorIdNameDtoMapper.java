package com.daswort.web.mapper;

import com.daswort.core.entity.Author;
import com.daswort.web.dto.IdNameDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class AuthorIdNameDtoMapper {
    public static IdNameDto toIdNameDto(Author author) {
        return Optional.ofNullable(author).map(a ->
                IdNameDto.builder()
                        .id(a.getId())
                        .name(String.join(" ", a.getFirstName(), a.getLastName()))
                        .build()
        ).orElse(null);
    }

    public static List<IdNameDto> toIdNameDto(List<Author> authorList) {
        return ofNullable(authorList)
                .map(authors -> authors.stream()
                        .map(AuthorIdNameDtoMapper::toIdNameDto).collect(Collectors.toList()))
                .orElse(List.of());
    }
}
