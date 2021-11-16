package com.daswort.web.http;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

import static java.util.Optional.ofNullable;

@Data
@Builder
public class ContentDispositionBuilder {

    private String filename;

    @Override
    public String toString() {
        return String.format("attachment; filename=\"%s\"", ofNullable(filename).orElse(Instant.now().toString()));
    }

}
