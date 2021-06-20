package com.daswort.core.model;

import com.daswort.core.entity.Author;
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
    Author melody;
    Author arrangement;
    Author adaptation;


    public Optional<IdName> getComposition() {
        return ofNullable(composition);
    }

    public Optional<List<IdName>> getTopics() {
        return ofNullable(topics);
    }

    public Optional<Author> getMelody() {
        return ofNullable(melody);
    }

    public Optional<Author> getArrangement() {
        return ofNullable(arrangement);
    }

    public Optional<Author> getAdaptation() {
        return ofNullable(adaptation);
    }
}
