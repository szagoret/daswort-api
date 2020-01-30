package com.daswort.web.mapper;

import com.daswort.core.entity.Author;
import com.daswort.web.dto.AuthorDto;

import java.util.Optional;

public class AuthorDtoMapper {

    public static AuthorDto toAuthorDto(Author author) {
        return Optional.ofNullable(author).map(a ->
                AuthorDto.builder()
                        .id(a.getId())
                        .firstName(a.getFirstName())
                        .lastName(a.getLastName())
                        .build()
        ).orElse(null);
    }
}
