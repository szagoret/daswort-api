package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Author {
    @Id
    private String id;
    private String firstName;
    private String lastName;
}
