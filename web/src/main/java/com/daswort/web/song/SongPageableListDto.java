package com.daswort.web.song;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SongPageableListDto {
    private List<SongDto> songs;
    private int size;
    private int page;
    private long total;
}
