package com.daswort.web.dto.song;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongDto {
    private String id;
    private String name;
}
