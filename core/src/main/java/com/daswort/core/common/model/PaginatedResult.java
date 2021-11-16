package com.daswort.core.common.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PaginatedResult<T> {
    @NonNull
    List<T> content;
    @NonNull
    Long totalCount;
}
