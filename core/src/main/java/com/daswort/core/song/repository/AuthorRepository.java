package com.daswort.core.song.repository;

import com.daswort.core.song.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {
}
