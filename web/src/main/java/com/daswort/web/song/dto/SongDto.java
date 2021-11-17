package com.daswort.web.song.dto;

import com.daswort.core.song.domain.SongFile;
import com.daswort.core.song.domain.Language;
import com.daswort.web.common.IdTitleDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.NonNull;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SongDto {
    String code;
    @NonNull
    String title;
    String originalTitle;
    List<@Valid IdTitleDto> instruments;
    List<@Valid IdTitleDto> vocals;
    List<@Valid IdTitleDto> topics;
    List<AuthorDto> composers;
    List<AuthorDto> arrangers;
    List<AuthorDto> orchestrators;
    List<AuthorDto> translators;
    LocalDate publishDate;
    Language language;
    List<SongFile> files;
}
