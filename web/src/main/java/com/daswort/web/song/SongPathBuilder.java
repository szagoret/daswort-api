package com.daswort.web.song;

import com.daswort.core.entity.Category;
import com.daswort.web.idname.IdNameDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class SongPathBuilder {

    public static List<IdNameDto> buildPath(List<Category> categories) {
        Objects.requireNonNull(categories);
        return categories.stream().map(category ->
                IdNameDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .collect(toList());
    }
}
