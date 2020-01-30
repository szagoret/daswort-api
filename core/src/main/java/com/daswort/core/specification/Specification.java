package com.daswort.core.specification;

import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import java.util.Optional;

public interface Specification<T> {
    Optional<CriteriaDefinition> toCriteriaDefinition(T t);
}
