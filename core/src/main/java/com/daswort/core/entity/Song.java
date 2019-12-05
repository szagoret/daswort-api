package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Data
@Builder
@Document
public class Song {
    @Id
    private String id;

    @TextIndexed
    private String name;

    private List<File> files;

    private String categoryId;

    @Indexed
    private Set<String> tags;
}
