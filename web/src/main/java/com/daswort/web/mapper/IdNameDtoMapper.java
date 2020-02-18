package com.daswort.web.mapper;

import com.daswort.core.entity.IdName;
import com.daswort.web.dto.IdNameDto;

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
}
