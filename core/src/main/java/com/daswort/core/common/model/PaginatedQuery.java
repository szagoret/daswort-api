package com.daswort.core.common.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Builder
@Value
public class PaginatedQuery<T> {
    @NonNull
    T criteria;
    Pageable pageable;

    public Pageable getPageable() {
        return Optional.ofNullable(pageable).orElse(Pageable.unpaged());
    }
}
