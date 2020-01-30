package com.daswort.core.specification;

import com.daswort.core.model.SongSearch;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class SongSearchSpecification implements Specification<SongSearch> {

    @Override
    public Optional<CriteriaDefinition> toCriteriaDefinition(SongSearch songSearch) {
        final List<Criteria> criteriaFilters = new ArrayList<>();

        // by name
        songSearch.getName().ifPresent(name -> criteriaFilters.add(where("name").is(name)));

        // by category
        songSearch.getCategoryId().ifPresent(categoryId -> criteriaFilters.add(where("category._id").is(categoryId)));

        // by tags
        if (songSearch.getTagsIds().size() > 0) {
            List<Criteria> tagsCriteriaList = songSearch.getTagsIds()
                    .stream()
                    .map(tagId -> where("tags").elemMatch(where("_id").is(tagId)))
                    .collect(toList());
            criteriaFilters.addAll(tagsCriteriaList);
        }

        // by topics
        if (songSearch.getTopicsIds().size() > 0) {
            criteriaFilters.add(where("topics").elemMatch(where("_id").in(songSearch.getTopicsIds())));
        }

        // by melody author
        if (songSearch.getMelodyAuthorsIds().size() > 0) {
            criteriaFilters.add(where("melody._id").in(songSearch.getMelodyAuthorsIds()));
        }

        // by composition
        if (songSearch.getCompositionsIds().size() > 0) {
            criteriaFilters.add(where("composition._id").in(songSearch.getCompositionsIds()));
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
