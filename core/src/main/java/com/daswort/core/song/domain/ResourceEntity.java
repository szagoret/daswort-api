package com.daswort.core.song.domain;

import java.time.Instant;

public interface ResourceEntity {
    String getCode();

    Long getSize();

    Instant getUploadedAt();

    String getExtension();
}
