package com.daswort.core.song.command;

import com.daswort.core.song.domain.*;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SaveSongCommand {
    String code;
    String title;
    String originalTitle;
    Language language;
    List<Instrument> instruments;
    List<Vocal> vocals;
    List<Topic> topics;
    List<Author> composers;
    List<Author> arrangers;
    List<Author> orchestrators;
    List<Author> translators;
}
