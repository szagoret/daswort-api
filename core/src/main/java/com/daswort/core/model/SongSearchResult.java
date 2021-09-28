package com.daswort.core.model;

import com.daswort.core.entity.Song;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class SongSearchResult implements Serializable {
    private List<Song> songList;
    private Pageable pageable;
    private Long totalCount;
    private String sortDirection;
    private String sortProperty;
}
