package com.daswort.web.mapper;

import com.daswort.core.entity.Author;
import com.daswort.web.dto.AuthorDto;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class AuthorDtoMapper {

    public static AuthorDto toAuthorDto(Author author) {
        return ofNullable(author).map(a ->
                AuthorDto.builder()
                        .id(a.getId())
                        .firstName(a.getFirstName())
                        .lastName(a.getLastName())
                        .build()
        ).orElse(null);
    }

    public static List<AuthorDto> toAuthorDto(List<Author> authorList) {
        return ofNullable(authorList)
                .map(authors -> authors.stream()
                        .map(AuthorDtoMapper::toAuthorDto).collect(Collectors.toList()))
                .orElse(List.of());
    }
}
