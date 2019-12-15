package com.daswort.web.dto.song;

import com.daswort.core.entity.Song;
import com.daswort.web.dto.breadcrumb.Breadcrumb;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongSearchSuggestion {
    private Breadcrumb breadcrumb;
    private Song song;
}
