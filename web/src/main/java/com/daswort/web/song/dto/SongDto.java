package com.daswort.web.song.dto;

import com.daswort.core.song.domain.Language;
import com.daswort.core.song.domain.SongFile;
import com.daswort.web.common.IdTitleDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SongDto {
    private String code;
    @NonNull
    private String title;
    private String originalTitle;
    private List<@Valid IdTitleDto> instruments;
    private List<@Valid IdTitleDto> vocals;
    private List<@Valid IdTitleDto> topics;
    private List<AuthorDto> composers;
    private List<AuthorDto> arrangers;
    private List<AuthorDto> orchestrators;
    private List<AuthorDto> translators;
    private LocalDate publishDate;
    private Language language;
    private List<SongFile> files;
}
