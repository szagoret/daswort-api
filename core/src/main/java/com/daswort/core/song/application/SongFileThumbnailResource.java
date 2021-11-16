package com.daswort.core.song.application;

import com.daswort.core.song.domain.Thumbnail;
import com.daswort.core.storage.EntityFileResource;
import com.daswort.core.storage.FileResource;
import com.daswort.core.storage.FileResourceBytes;

public class SongFileThumbnailResource extends FileResourceBytes implements EntityFileResource<Thumbnail> {
    final Thumbnail thumbnail;

    public SongFileThumbnailResource(FileResource fileResource, Thumbnail thumbnail) {
        super(fileResource.getBytes(), fileResource.getContentType());
        this.thumbnail = thumbnail;
    }

    @Override
    public Thumbnail getEntity() {
        return thumbnail;
    }
}
