package com.daswort.web.idname;

import com.daswort.core.entity.Author;
import com.daswort.core.entity.IdName;

import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class IdNameDtoMapper {

    public static IdNameDto toIdNameDto(IdName idName) {

        return ofNullable(idName).map(idN ->
                IdNameDto.builder()
                        .id(idN.getId())
                        .name(idN.getName())
                        .build())
                .orElse(IdNameDto.builder().build());
    }

    public static List<IdNameDto> toIdNameDto(List<IdName> idNameList) {
        return ofNullable(idNameList)
                .map(idNames -> idNames.stream().map(IdNameDtoMapper::toIdNameDto).collect(toList()))
                .orElse(List.of());

    }

    public static IdNameDto toIdNameDto(Author authorDto) {
        return IdNameDto.builder()
                .id(authorDto.getId())
                .name(String.format("%s %s", authorDto.getFirstName(), authorDto.getLastName()))
                .build();
    }
}
