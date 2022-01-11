package com.daswort.core.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private String username;

    private String passwordHash;

    private Instant createdTs;

    private Instant updatedTs;

}
