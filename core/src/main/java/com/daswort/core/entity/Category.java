package com.daswort.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    //    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String name;
    private String parentId;
}
