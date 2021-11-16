package com.daswort.core.song.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@Getter
@Builder
@Document
@EqualsAndHashCode(of = "code")
public class Thumbnail implements ResourceEntity, Serializable {
    @NonNull
    String code;
    @NonNull
    String type;
    @NonNull
    Long size;
    @Builder.Default
    Instant uploadedAt = Instant.now();
    @Builder.Default
    Integer sequence = 0;
    String extension;
}
