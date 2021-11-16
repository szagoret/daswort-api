package com.daswort.core.storage;

import com.daswort.core.song.domain.ResourceEntity;

public interface EntityFileResource<T extends ResourceEntity> extends FileResource {
    T getEntity();
}
