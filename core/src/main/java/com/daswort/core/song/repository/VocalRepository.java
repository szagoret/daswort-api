package com.daswort.core.song.repository;

import com.daswort.core.song.domain.Vocal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocalRepository extends CrudRepository<Vocal, String> {
}
