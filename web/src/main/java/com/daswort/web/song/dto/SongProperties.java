package com.daswort.web.song.dto;

import com.daswort.core.song.domain.Author;
import com.daswort.core.song.domain.Instrument;
import com.daswort.core.song.domain.Topic;
import com.daswort.core.song.domain.Vocal;

import java.util.List;

public record SongProperties(List<Instrument> instruments,
                             List<Topic> topics,
                             List<Vocal> vocals,
                             List<Author> authors) {
}
