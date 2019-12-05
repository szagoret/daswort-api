package com.daswort.core.repository;

import com.daswort.core.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, String> {
}
