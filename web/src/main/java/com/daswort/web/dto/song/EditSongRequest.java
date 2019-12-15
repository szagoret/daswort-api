package com.daswort.web.dto.song;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class EditSongRequest {
    String name;
    String categoryId;
    Set<String> tagsIds;
    String compositionId;
    String partitionId;
    Set<String> instrumentsIds;
    String difficultyId;
    LocalDate writtenOn;
    Set<String> topicsIds;
    String melodyId;
    String arrangementId;
    String adaptationId;
}
