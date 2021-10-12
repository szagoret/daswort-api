package com.daswort.web.song;

import com.daswort.web.idname.IdNameDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongFiltersDto {
    private List<IdNameDto> topics;
    private List<IdNameDto> compositions;
    private List<IdNameDto> difficulties;
    private List<IdNameDto> instruments;
    private List<IdNameDto> authors;
    private List<IdNameDto> melodies;
    private List<IdNameDto> arrangements;
    private List<IdNameDto> adaptations;
}