package com.daswort.web.author;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    private String id;
    private String firstName;
    private String lastName;
}