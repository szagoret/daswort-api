package com.daswort.web.song.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthorDto {
    String id;
    String firstName;
    String lastName;
}
