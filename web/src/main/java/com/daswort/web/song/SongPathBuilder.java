package com.daswort.web.song;

import com.daswort.core.song.domain.Category;
import com.daswort.web.common.IdTitleDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class SongPathBuilder {

    public static List<IdTitleDto> buildPath(List<Category> categories) {
        Objects.requireNonNull(categories);
        return categories.stream().map(category ->
                        IdTitleDto.builder()
                                .id(category.getId())
                                .title(category.getName())
                                .build())
                .collect(toList());
    }
}
