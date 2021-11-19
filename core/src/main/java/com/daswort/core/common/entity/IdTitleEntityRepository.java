package com.daswort.core.common.entity;

import com.daswort.core.common.model.IdTitleEntity;

import java.util.List;
import java.util.Optional;

public interface IdTitleEntityRepository<T extends IdTitleEntity> {
    Optional<T> findById(String id);

    T save(String id, String title);

    List<T> findAll();

    void delete(String id);

}
