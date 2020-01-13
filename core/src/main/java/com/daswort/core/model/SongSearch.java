package com.daswort.core.model;

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
public class SongSearch {
    private String name;
    private String categoryId;
    private Set<String> tagsIds = new HashSet<>();
    private String compositionId;
    private String partitionId;
    private Set<String> instrumentsIds = new HashSet<>();
    private String difficultyId;
    private Set<String> topicsIds = new HashSet<>();
    private String melodyAuthorId;
    private String arrangementId;
    private String adaptationId;

    public Optional<String> getName() {
        return ofNullable(name);
    }

    public Optional<String> getCategoryId() {
        return ofNullable(categoryId);
    }

    public Set<String> getTagsIds() {
        return tagsIds;
    }

    public Optional<String> getCompositionId() {
        return ofNullable(compositionId);
    }

    public Optional<String> getPartitionId() {
        return ofNullable(partitionId);
    }

    public Set<String> getInstrumentsIds() {
        return instrumentsIds;
    }

    public Optional<String> getDifficultyId() {
        return ofNullable(difficultyId);
    }

    public Set<String> getTopicsIds() {
        return topicsIds;
    }

    public Optional<String> getMelodyAuthorId() {
        return ofNullable(melodyAuthorId);
    }

    public Optional<String> getArrangementId() {
        return ofNullable(arrangementId);
    }

    public Optional<String> getAdaptationId() {
        return ofNullable(adaptationId);
    }
}
