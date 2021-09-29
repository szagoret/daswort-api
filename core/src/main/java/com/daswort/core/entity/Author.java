package com.daswort.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {
    //    @MongoId(FieldType.OBJECT_ID)
    @MongoId
    private String id;
    private String firstName;
    private String lastName;
}
