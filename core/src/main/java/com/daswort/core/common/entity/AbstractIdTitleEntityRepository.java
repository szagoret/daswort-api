package com.daswort.core.common.entity;

import com.daswort.core.common.model.IdTitleEntity;
import org.springframework.data.mongodb.core.MongoOperations;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class AbstractIdTitleEntityRepository<T extends IdTitleEntity> implements IdTitleEntityRepository<T> {
    private final MongoOperations operations;
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractIdTitleEntityRepository(MongoOperations operations) {
        this.operations = operations;
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(operations.findById(id, type));
    }

    @Override
    public T save(String id, String title) {
        try {
            return operations.save(type.getDeclaredConstructor(String.class, String.class).newInstance(id, title));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<T> findAll() {
        return operations.findAll(type);
    }

    @Override
    public void delete(String id) {
        operations.remove(type).matching(query(where("id").is(id))).one();
    }
}
