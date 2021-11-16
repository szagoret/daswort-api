package com.daswort.core.song.repository;

import com.daswort.core.song.domain.Category;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, String> {

    @Query("{ 'parentId' : ?0 }")
    List<Category> findCategoriesByParentId(String parentId);
}
