package com.daswort.core.song.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongSearchQuery {
    private String title;
    private Set<String> instrumentsIds = new HashSet<>();
    private Set<String> topicsIds = new HashSet<>();
    private Set<String> vocalsIds = new HashSet<>();
    private Set<String> composersIds = new HashSet<>();
    private Set<String> arrangersIds = new HashSet<>();
    private Set<String> orchestratorsIds = new HashSet<>();

    public Optional<String> getName() {
        return ofNullable(title);
    }

}
