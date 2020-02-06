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
    private String partitionId;
    private Set<String> instrumentsIds = new HashSet<>();
    private Set<String> difficultiesIds = new HashSet<>();
    private Set<String> topicsIds = new HashSet<>();
    private Set<String> melodyAuthorsIds = new HashSet<>();
    private String arrangementId;
    private String adaptationId;

    @NonNull
    private Integer page;
    @NonNull
    private Integer size;

    public Optional<String> getName() {
        return ofNullable(name);
    }

    public Optional<String> getCategoryId() {
        return ofNullable(categoryId);
    }

    public Set<String> getTagsIds() {
        return tagsIds;
    }

    public Set<String> getCompositionsIds() {
        return compositionsIds;
    }

    public Optional<String> getPartitionId() {
        return ofNullable(partitionId);
    }

    public Set<String> getInstrumentsIds() {
        return instrumentsIds;
    }

    public Set<String> getDifficultiesIds() {
        return difficultiesIds;
    }

    public Set<String> getTopicsIds() {
        return topicsIds;
    }

    public Set<String> getMelodyAuthorsIds() {
        return melodyAuthorsIds;
    }

    public Optional<String> getArrangementId() {
        return ofNullable(arrangementId);
    }

    public Optional<String> getAdaptationId() {
        return ofNullable(adaptationId);
    }

    @NonNull
    public Integer getPage() {
        return page;
    }

    @NonNull
    public Integer getSize() {
        return size;
    }
}
