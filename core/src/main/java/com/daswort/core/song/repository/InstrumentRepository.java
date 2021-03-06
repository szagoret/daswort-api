package com.daswort.core.song.repository;

import com.daswort.core.song.domain.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrument, String> {
}
