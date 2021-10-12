package com.daswort.web.song;

import com.daswort.web.idname.IdNameDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongSearchSuggestion {
    private List<IdNameDto> path;
    private SongDto song;
}
