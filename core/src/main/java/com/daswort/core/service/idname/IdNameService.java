package com.daswort.core.service.idname;

import com.daswort.core.entity.IdName;
import com.daswort.core.entity.IdNameCollection;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
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

    public List<IdName> getAll(IdNameCollection collection) {
        Objects.requireNonNull(collection);
        return mongoOperations.findAll(IdName.class, collection.getName());
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

    public void removeIdNameItem(IdNameCollection collection, String id) {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(id);
        mongoOperations.remove(query(where("_id").is(id)), collection.getName());
    }

    public IdName updateIdName(IdNameCollection collection, String itemId, IdName updateIdName) {
        Objects.requireNonNull(collection);
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(updateIdName);

        final var update = new Update().set("name", updateIdName.getName());
        final var options = FindAndModifyOptions.options().returnNew(true);

        return mongoOperations.findAndModify(query(where("_id").is(itemId)), update, options, IdName.class, collection.getName());
    }
}
