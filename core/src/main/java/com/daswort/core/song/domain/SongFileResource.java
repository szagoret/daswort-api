package com.daswort.core.song.domain;

import com.daswort.core.song.domain.SongFile;
import com.daswort.core.storage.EntityFileResource;
import com.daswort.core.storage.FileResource;
import com.daswort.core.storage.FileResourceBytes;

public class SongFileResource extends FileResourceBytes implements EntityFileResource<SongFile> {
    final SongFile songFile;

    public SongFileResource(FileResource fileResource, SongFile songFile) {
        super(fileResource.getBytes(), fileResource.getContentType());
        this.songFile = songFile;
    }

    @Override
    public SongFile getEntity() {
        return songFile;
    }
}
