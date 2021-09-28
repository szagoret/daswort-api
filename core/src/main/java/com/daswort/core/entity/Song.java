package com.daswort.core.entity;

import com.daswort.core.annotation.CollectionName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Song implements Serializable {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String name;

    private List<File> files;

    private Category category;

    @CollectionName(IdNameCollection.Constants.tag)
    private List<IdName> tags;

    @CollectionName(IdNameCollection.Constants.composition)
    private IdName composition;

    @CollectionName(IdNameCollection.Constants.partition)
    private IdName partition;

    @CollectionName(IdNameCollection.Constants.instrument)
    private List<IdName> instruments;

    @CollectionName(IdNameCollection.Constants.difficulty)
    private IdName difficulty;

    private LocalDate writtenOn;

    private Instant createdAt;

    @CollectionName(IdNameCollection.Constants.topic)
    private List<IdName> topics;

    private Author melody;
    private Author arrangement;
    private Author adaptation;
}
