package com.daswort.core.service.song;

import com.daswort.core.entity.Song;
import com.daswort.core.service.EntityUpdateService;
import org.springframework.stereotype.Service;

@Service
public class SongUpdateService implements EntityUpdateService<Song> {

    @Override
    public Song update(Song song) {
        return null;
    }
}
