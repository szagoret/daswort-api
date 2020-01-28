package com.daswort.web.mapper;

import com.daswort.core.entity.IdName;
import com.daswort.web.dto.IdNameDto;

public class IdNameDtoMapper {

    public static IdNameDto toIdNameDto(IdName idName) {
        return IdNameDto.builder()
                .id(idName.getId())
                .name(idName.getName())
                .build();
    }
}
