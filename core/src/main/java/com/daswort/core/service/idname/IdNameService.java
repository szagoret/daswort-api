package com.daswort.core.service.idname;

import com.daswort.core.entity.IdName;
import com.daswort.core.service.IdNameCollection;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class IdNameService {

    private final MongoOperations mongoOperations;

    public IdNameService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public IdName getById(IdNameCollection collection, String id) {
        Objects.requireNonNull(collection, id);
        return mongoOperations.findOne(query(where("id").is(id)), IdName.class, collection.name());
    }

    public List<IdName> getAllByIds(IdNameCollection collection, Set<String> ids) {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(ids);
        return mongoOperations.find(query(where("id").in(ids)), IdName.class, collection.name());
    }
}
