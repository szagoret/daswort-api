package com.daswort.core.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@EqualsAndHashCode
@Builder
@Document
@Data
public class File implements Serializable {
    String name;
    String extension;
    String code;
    String path;
    Long size;
    Set<String> lgThumbnails;
    Set<String> smThumbnails;
    Boolean primary;
    Instant uploadedAt;
}
