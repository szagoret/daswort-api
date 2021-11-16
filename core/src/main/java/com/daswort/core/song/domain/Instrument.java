package com.daswort.core.song.domain;

import com.daswort.core.common.model.IdTitleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Instrument implements IdTitleEntity, Serializable {
    String id;
    String title;
}
