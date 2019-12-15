package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Song {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @TextIndexed
    private String name;
    private List<File> files;
    private Category category;
    private Set<IdName> tags;
    private IdName composition;
    private IdName partition;
    private List<IdName> instruments;
    private IdName difficulty;
    private LocalDate writtenOn;
    private Set<IdName> topics;
    private Author melody;
    private Author arrangement;
    private Author adaptation;

}
