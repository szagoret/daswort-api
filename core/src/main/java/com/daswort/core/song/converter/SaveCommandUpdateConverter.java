package com.daswort.core.song.converter;

import com.daswort.core.song.command.SaveSongCommand;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.time.LocalDate;

public class SaveCommandUpdateConverter {
    public static UpdateDefinition convert(String songCode, SaveSongCommand command) {
        return new Update()
                .set("code", songCode)
                .set("title", command.getTitle())
                .set("originalTitle", command.getOriginalTitle())
                .set("lng", command.getLanguage())
                .set("instruments", command.getInstruments())
                .set("vocals", command.getVocals())
                .set("topics", command.getTopics())
                .set("composers", command.getComposers())
                .set("arrangers", command.getArrangers())
                .set("orchestrators", command.getOrchestrators())
                .set("translators", command.getTranslators())
                .setOnInsert("publishDate", LocalDate.now());
    }
}
