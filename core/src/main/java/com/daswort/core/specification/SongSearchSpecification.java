package com.daswort.core.specification;

import com.daswort.core.song.query.SongSearchQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class SongSearchSpecification implements Specification<SongSearchQuery> {

    private static List<Criteria> buildArrayMatchCriteria(String fieldName, Set<String> ids) {
        requireNonNull(fieldName);
        requireNonNull(ids);
        return ids.stream().map(id -> where(fieldName).elemMatch(where("_id").is(id))).collect(toList());
    }

    @Override
    public Optional<CriteriaDefinition> toCriteriaDefinition(SongSearchQuery songSearchQuery) {
        final List<Criteria> criteriaFilters = new ArrayList<>();

        // by title
        songSearchQuery.getName().map(String::trim).filter(not(String::isBlank)).ifPresent(name -> criteriaFilters.add(where("title").regex(name, "i")));

        // by instruments
        if (songSearchQuery.getInstrumentsIds().size() > 0) {
            criteriaFilters.addAll(buildArrayMatchCriteria("instruments", songSearchQuery.getInstrumentsIds()));
        }

        // by vocals
        if (songSearchQuery.getInstrumentsIds().size() > 0) {
            criteriaFilters.addAll(buildArrayMatchCriteria("vocals", songSearchQuery.getVocalsIds()));
        }

        // by topics
        if (songSearchQuery.getTopicsIds().size() > 0) {
            criteriaFilters.addAll(buildArrayMatchCriteria("topics", songSearchQuery.getTopicsIds()));
        }

        // by composers
        if (songSearchQuery.getComposersIds().size() > 0) {
            criteriaFilters.addAll(buildArrayMatchCriteria("composers", songSearchQuery.getComposersIds()));
        }

        // build result search criteria
        return criteriaFilters.size() > 0
                ? of(new Criteria().andOperator(criteriaFilters.toArray(Criteria[]::new)))
                : empty();
    }
}
