package com.daswort.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Author {
//    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String firstName;
    private String lastName;
}
