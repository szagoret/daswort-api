package com.daswort.core.song.repository;

import com.daswort.core.song.domain.SongFile;

import java.util.List;
import java.util.Optional;

public interface SongFileRepository {
    Optional<SongFile> getSongFile(String songCode, String fileCode);

    List<SongFile> getSongFiles(String songCode);

    Optional<SongFile> saveSongFile(String songCode, SongFile songFile);

    void makeFilePrimary(String songCode, String fileCode, boolean isPrimary);
}
