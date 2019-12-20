package com.daswort.core.model;

import lombok.Builder;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

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
        return Optional.ofNullable(name);
    }

    public Optional<String> getCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public Optional<Set<String>> getTagsIds() {
        return Optional.ofNullable(tagsIds);
    }

    public Optional<String> getCompositionId() {
        return Optional.ofNullable(compositionId);
    }

    public Optional<String> getPartitionId() {
        return Optional.ofNullable(partitionId);
    }

    public Optional<Set<String>> getInstrumentsIds() {
        return Optional.ofNullable(instrumentsIds);
    }

    public Optional<String> getDifficultyId() {
        return Optional.ofNullable(difficultyId);
    }

    public Optional<LocalDate> getWrittenOn() {
        return Optional.ofNullable(writtenOn);
    }

    public Optional<Set<String>> getTopicsIds() {
        return Optional.ofNullable(topicsIds);
    }

    public Optional<String> getMelodyId() {
        return Optional.ofNullable(melodyId);
    }

    public Optional<String> getArrangementId() {
        return Optional.ofNullable(arrangementId);
    }

    public Optional<String> getAdaptationId() {
        return Optional.ofNullable(adaptationId);
    }
}
