package com.daswort.core.service.storage;

import com.daswort.core.song.domain.SongFile;
import com.daswort.core.song.domain.Thumbnail;
import com.daswort.core.song.query.SongFileQuery;

public final class SongFilePathResolver {

    public static String resolve(SongFileQuery query, Thumbnail thumbnail) {
        return SongFilePathBuilder.build(query.songCode(), query.fileCode(), thumbnail.getCode());
    }

    public static String resolve(String songCode, SongFile songFile) {
        return SongFilePathBuilder.build(songCode, songFile.getCode());
    }
}
