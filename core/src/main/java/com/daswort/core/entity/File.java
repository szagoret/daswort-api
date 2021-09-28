package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode
@Builder
@Document
@Data
public class File implements Serializable {
    String name;
    String extension;
    String fileCode;
    Long size;
    Set<String> thumbnails;
    Boolean primary;

    public void addThumbnail(String thumbnailCode) {
        this.thumbnails = new HashSet<>(thumbnails);
        thumbnails.add(thumbnailCode);
    }
}
