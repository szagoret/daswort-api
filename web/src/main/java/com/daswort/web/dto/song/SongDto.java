package com.daswort.web.dto.song;

import com.daswort.core.entity.File;
import com.daswort.web.dto.AuthorDto;
import com.daswort.web.dto.IdNameDto;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class SongDto {
    private String id;
    private String name;
    private IdNameDto composition;
    private IdNameDto difficulty;
    private List<IdNameDto> topics;
    private List<IdNameDto> instruments;
    private AuthorDto arrangement;
    private AuthorDto adaptation;
    private AuthorDto melody;
    private Instant createdAt;
    private List<File> files;
}
