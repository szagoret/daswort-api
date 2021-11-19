package com.daswort.web.song;

import com.daswort.web.common.IdTitleDto;
import com.daswort.web.song.dto.SongDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongSearchSuggestion {
    private List<IdTitleDto> path;
    private SongDto song;
}
