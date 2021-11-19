package com.daswort.core.song.domain;

import com.daswort.core.common.entity.EntitySequenceName;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Builder
@Document
@EqualsAndHashCode(of = "code")
public class SongFile implements ResourceEntity, Serializable {
    @Transient
    public static final EntitySequenceName SEQUENCE = EntitySequenceName.file;
    @NonNull
    String name;
    @NonNull
    String code;
    @NonNull
    Long size;
    @Builder.Default
    Instant uploadedAt = Instant.now();
    String extension;
    @Builder.Default
    Boolean primary = false;
    @Builder.Default
    List<Thumbnail> thumbnails = List.of();
}
