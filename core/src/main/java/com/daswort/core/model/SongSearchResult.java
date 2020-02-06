package com.daswort.core.model;

import com.daswort.core.entity.Song;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
public class SongSearchResult {
    private List<Song> songList;
    private Pageable pageable;
    private Long totalCount;
}
