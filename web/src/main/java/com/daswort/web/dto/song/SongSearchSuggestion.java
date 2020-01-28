package com.daswort.web.dto.song;

import com.daswort.web.dto.IdNameDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongSearchSuggestion {
    private List<IdNameDto> path;
    private SongDto song;
}
