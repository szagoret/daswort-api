package com.daswort.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongSearch {
    private String name;
    private String categoryId;
    private Set<String> tagsIds = new HashSet<>();
    private Set<String> compositionsIds = new HashSet<>();
    private Set<String> instrumentsIds = new HashSet<>();
    private Set<String> difficultiesIds = new HashSet<>();
    private Set<String> topicsIds = new HashSet<>();
    private Set<String> authorsIds = new HashSet<>();

    @NonNull
    private Integer page;
    @NonNull
    private Integer size;

    private String sortDirection;
    private String sortProperty;

    public Optional<String> getName() {
        return ofNullable(name);
    }
    public Optional<String> getCategoryId() {
        return ofNullable(categoryId);
    }

}
