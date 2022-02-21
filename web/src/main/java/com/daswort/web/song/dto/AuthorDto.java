package com.daswort.web.song.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorDto {
    private String id;
    private String name;

    @Builder
    public AuthorDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
