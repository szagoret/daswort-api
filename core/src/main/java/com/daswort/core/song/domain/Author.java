package com.daswort.core.song.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
}
