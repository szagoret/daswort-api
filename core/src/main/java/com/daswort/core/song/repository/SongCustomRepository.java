package com.daswort.core.song.repository;

import com.daswort.core.song.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

public interface SongCustomRepository {
    Song save(String songCode, UpdateDefinition updateDefinition);

    Page<Song> findAll(Query query, Pageable pageable);

    void updateRef(Author author);

    void updateRef(Instrument instrument);

    void updateRef(Vocal vocal);

    void updateRef(Topic topic);

    boolean isReferencedBy(Author author);

    boolean isReferencedBy(Instrument instrument);

    boolean isReferencedBy(Vocal vocal);

    boolean isReferencedBy(Topic topic);
}
