package com.daswort.web.song.dto;

import com.daswort.web.common.IdTitleDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongSearchSuggestionDto {
    private List<IdTitleDto> path;
    private SongDto song;
}
