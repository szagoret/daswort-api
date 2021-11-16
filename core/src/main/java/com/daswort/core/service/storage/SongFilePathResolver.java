package com.daswort.core.service.storage;

import com.daswort.core.song.domain.SongFile;
import com.daswort.core.song.domain.Thumbnail;

public final class SongFilePathResolver {

    public static String resolve(String songCode, String fileCode, Thumbnail thumbnail) {
        return SongFilePathBuilder.build(songCode, fileCode, thumbnail.getCode());
    }

    public static String resolve(String songCode, SongFile songFile) {
        return SongFilePathBuilder.build(songCode, songFile.getCode());
    }
}
