package com.daswort.web.author;

import com.daswort.core.song.domain.Author;

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
        ).orElse(AuthorDto.builder().build());
    }

    public static List<AuthorDto> toAuthorDto(List<Author> authorList) {
        return ofNullable(authorList)
                .map(authors -> authors.stream()
                        .map(AuthorDtoMapper::toAuthorDto).collect(Collectors.toList()))
                .orElse(List.of());
    }
}
