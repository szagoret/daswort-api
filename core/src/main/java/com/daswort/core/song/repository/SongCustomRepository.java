package com.daswort.core.song.repository;

import com.daswort.core.song.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

public interface SongCustomRepository {
    Song save(String songCode, UpdateDefinition updateDefinition);

    Page<Song> findAll(Query query, Pageable pageable);

    void updateAuthorRefs(Author author);

    void updateInstrumentRef(Instrument instrument);

    void updateVocalRef(Vocal vocal);

    void updateTopicRef(Topic topic);

    boolean isReferencedByAuthor(Author author);
}
