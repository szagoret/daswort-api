package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document
public class Composition {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
}
