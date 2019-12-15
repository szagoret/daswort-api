package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@EqualsAndHashCode(of = "id")
@Document
public class IdName {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
}
