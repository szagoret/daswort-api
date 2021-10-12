package com.daswort.web.song;

import com.daswort.core.entity.File;
import com.daswort.web.idname.IdNameDto;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class SongDto {
    private String code;
    private String name;
    private IdNameDto composition;
    private IdNameDto difficulty;
    private List<IdNameDto> topics;
    private List<IdNameDto> instruments;
    private IdNameDto arrangement;
    private IdNameDto adaptation;
    private IdNameDto melody;
    private Instant createdAt;
    private List<File> files;
}