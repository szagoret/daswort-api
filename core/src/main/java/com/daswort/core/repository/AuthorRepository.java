package com.daswort.core.repository;

import com.daswort.core.song.domain.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, String> {
    @Override
    List<Author> findAll();
}
