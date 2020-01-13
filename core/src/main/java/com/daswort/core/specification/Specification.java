package com.daswort.core.specification;

import org.springframework.data.mongodb.core.query.CriteriaDefinition;

public interface Specification<T> {
    CriteriaDefinition toCriteriaDefinition(T t);
}
