package com.daswort.core.service.idname;

import com.daswort.core.entity.IdName;
import com.daswort.core.service.IdNameCollection;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class IdNameService {

    private final MongoOperations mongoOperations;

    public IdNameService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public IdName getById(IdNameCollection collection, String id) {
        Objects.requireNonNull(collection, id);
        return mongoOperations
                .findOne(Query.query(
                        Criteria.where("id").is(id)), IdName.class, collection.name()
                );
    }
}
