package com.daswort.core.service.song;

import com.daswort.core.entity.File;
import com.daswort.core.storage.FileResource;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SongFileResource {
    FileResource fileResource;
    File file;
}
