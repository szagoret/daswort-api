package com.daswort.core.model;

import com.daswort.core.entity.IdName;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Value
@Builder
public class SongUpdate {

    @NonNull
    String name;
    IdName composition;
    List<IdName> topics;
    IdName melody;
    IdName arrangement;
    IdName adaptation;


    public Optional<IdName> getComposition() {
        return ofNullable(composition);
    }

    public Optional<List<IdName>> getTopics() {
        return ofNullable(topics);
    }

    public Optional<IdName> getMelody() {
        return ofNullable(melody);
    }

    public Optional<IdName> getArrangement() {
        return ofNullable(arrangement);
    }

    public Optional<IdName> getAdaptation() {
        return ofNullable(adaptation);
    }
}
