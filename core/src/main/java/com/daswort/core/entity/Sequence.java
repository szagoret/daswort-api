package com.daswort.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "dbSequence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sequence {

    @Id
    String id;

    @Field("value")
    Integer value;
}