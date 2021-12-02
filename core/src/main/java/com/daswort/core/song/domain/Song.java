package com.daswort.core.song.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
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

    @Indexed(unique = true)
    private String code;

    @TextIndexed
    private String title;

    private String originalTitle;

    private List<Instrument> instruments;

    private List<Vocal> vocals;

    private List<Topic> topics;

    private List<Author> composers;

    private List<Author> arrangers;

    private List<Author> orchestrators;

    private List<Author> translators;

    private List<SongFile> files;

    private LocalDate publishDate;

    private Language lng;

    private Instant createdAt;
}
