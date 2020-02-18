package com.daswort.core.model;

import lombok.Builder;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

@Setter
@Builder
public class SongUpdate {

    String name;
    String categoryId;
    Set<String> tagsIds;
    String compositionId;
    String partitionId;
    Set<String> instrumentsIds;
    String difficultyId;
    LocalDate writtenOn;
    Set<String> topicsIds;
    String melodyId;
    String arrangementId;
    String adaptationId;


    public Optional<String> getName() {
        return ofNullable(name);
    }

    public Optional<String> getCategoryId() {
        return ofNullable(categoryId);
    }

    public Optional<Set<String>> getTagsIds() {
        return ofNullable(tagsIds);
    }

    public Optional<String> getCompositionId() {
        return ofNullable(compositionId);
    }

    public Optional<String> getPartitionId() {
        return ofNullable(partitionId);
    }

    public Optional<Set<String>> getInstrumentsIds() {
        return ofNullable(instrumentsIds);
    }

    public Optional<String> getDifficultyId() {
        return ofNullable(difficultyId);
    }

    public Optional<LocalDate> getWrittenOn() {
        return ofNullable(writtenOn);
    }

    public Optional<Set<String>> getTopicsIds() {
        return ofNullable(topicsIds);
    }

    public Optional<String> getMelodyId() {
        return ofNullable(melodyId);
    }

    public Optional<String> getArrangementId() {
        return ofNullable(arrangementId);
    }

    public Optional<String> getAdaptationId() {
        return ofNullable(adaptationId);
    }
}
