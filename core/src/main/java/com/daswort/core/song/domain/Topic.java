package com.daswort.core.song.domain;

import com.daswort.core.common.model.IdTitleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Topic implements IdTitleEntity, Serializable {
    @MongoId(FieldType.OBJECT_ID)
    String id;
    String title;
}
