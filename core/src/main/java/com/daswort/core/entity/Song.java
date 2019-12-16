package com.daswort.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Song {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @TextIndexed
    private String name;
    private List<File> files;
    private Category category;
    private List<IdName> tags;
    private IdName composition;
    private IdName partition;
    private List<IdName> instruments;
    private IdName difficulty;
    private LocalDate writtenOn;
    private List<IdName> topics;
    private Author melody;
    private Author arrangement;
    private Author adaptation;

}
