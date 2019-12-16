package com.daswort.core.repository;

import com.daswort.core.entity.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, String> {
}
