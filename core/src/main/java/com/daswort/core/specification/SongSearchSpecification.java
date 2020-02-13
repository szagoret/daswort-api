package com.daswort.core.specification;

import com.daswort.core.model.SongSearch;
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
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class SongSearchSpecification implements Specification<SongSearch> {

    private static List<Criteria> buildArrayMatchCriteria(String fieldName, Set<String> ids) {
        requireNonNull(fieldName);
        requireNonNull(ids);
        return ids.stream().map(id -> where(fieldName).elemMatch(where("_id").is(id))).collect(toList());
    }

    @Override
    public Optional<CriteriaDefinition> toCriteriaDefinition(SongSearch songSearch) {
        final List<Criteria> criteriaFilters = new ArrayList<>();

        // by name
        songSearch.getName().ifPresent(name -> criteriaFilters.add(where("name").is(name)));

        // by category
        songSearch.getCategoryId().ifPresent(categoryId -> criteriaFilters.add(where("category._id").is(categoryId)));

        // by tags
        if (songSearch.getTagsIds().size() > 0) {
            criteriaFilters.addAll(buildArrayMatchCriteria("tags", songSearch.getTagsIds()));
        }

        // by topics
        if (songSearch.getTopicsIds().size() > 0) {
            criteriaFilters.addAll(buildArrayMatchCriteria("topics", songSearch.getTopicsIds()));
        }

        // by arrangement author
        if (songSearch.getAuthorsIds().size() > 0) {
            criteriaFilters.add(where("arrangement._id").in(songSearch.getAuthorsIds()));
        }

        // by composition
        if (songSearch.getCompositionsIds().size() > 0) {
            criteriaFilters.add(where("composition._id").in(songSearch.getCompositionsIds()));
        }

        // by instruments
        if (songSearch.getInstrumentsIds().size() > 0) {
            criteriaFilters.add(where("instruments").elemMatch(where("_id").in(songSearch.getInstrumentsIds())));
        }

        // by difficulty
        if (songSearch.getDifficultiesIds().size() > 0) {
            criteriaFilters.add(where("difficulty._id").in(songSearch.getDifficultiesIds()));
        }

        // build result search criteria
        return criteriaFilters.size() > 0
                ? of(new Criteria().andOperator(criteriaFilters.toArray(Criteria[]::new)))
                : empty();
    }
}
