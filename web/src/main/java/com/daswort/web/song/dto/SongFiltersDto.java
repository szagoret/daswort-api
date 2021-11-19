package com.daswort.web.song.dto;

import com.daswort.web.common.IdTitleDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongFiltersDto {
    private List<IdTitleDto> topics;
    private List<IdTitleDto> compositions;
    private List<IdTitleDto> difficulties;
    private List<IdTitleDto> instruments;
    private List<IdTitleDto> authors;
    private List<IdTitleDto> melodies;
    private List<IdTitleDto> arrangements;
    private List<IdTitleDto> adaptations;
}
