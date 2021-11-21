package com.daswort.core.song.repository.impl;

import com.daswort.core.song.domain.*;
import com.daswort.core.song.repository.SongCustomRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Fields.field;
import static org.springframework.data.mongodb.core.aggregation.Fields.fields;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@AllArgsConstructor
public class SongCustomRepositoryImpl implements SongCustomRepository {
    private final MongoOperations mongoOperations;

    @Override
    public Song save(String songCode, UpdateDefinition updateDefinition) {
        return mongoOperations.update(Song.class)
                .matching(query(where("code").is(songCode)))
                .apply(updateDefinition)
                .withOptions(FindAndModifyOptions.options().upsert(true).returnNew(true))
                .findAndModifyValue();
    }

    @Override
    public Page<Song> findAll(Query query, Pageable pageable) {
        final long count = mongoOperations.count(query, Song.class);
        final List<Song> songList = mongoOperations.find(query.with(pageable), Song.class);
        return new PageImpl<>(songList, pageable, count);
    }


    /*
db.song.update(
    {},
    {
        $set: {
            "arrangers.$[a].firstName": "FirstName",
            "arrangers.$[a].lastName": "LastName",
            "composers.$[c].firstName": "FirstName",
            "composers.$[c].lastName": "LastName",
            "orchestrators.$[o].firstName": "FirstName",
            "orchestrators.$[o].lastName": "LastName",
            "translators.$[t].firstName": "FirstName",
            "translators.$[t].lastName": "LastName"
        },
    },
    {
        arrayFilters: [
            {
                "a._id": "1"
            },
            {
                "c._id": "1"
            },
            {
                "o._id": "1"
            },
            {
                "t._id": "1"
            }
        ],
        multi: true
    }
);

     */
    @Override
    public void updateRef(Author author) {
        final var authorId = new ObjectId(author.getId());
        final var update = new Update()
                .set("arrangers.$[a].firstName", author.getFirstName())
                .set("arrangers.$[a].lastName", author.getLastName())
                .set("composers.$[c].firstName", author.getFirstName())
                .set("composers.$[c].lastName", author.getLastName())
                .set("orchestrators.$[o].firstName", author.getFirstName())
                .set("orchestrators.$[o].lastName", author.getLastName())
                .set("translators.$[t].firstName", author.getFirstName())
                .set("translators.$[t].lastName", author.getLastName())
                .filterArray(Criteria.where("a._id").is(authorId))
                .filterArray(Criteria.where("c._id").is(authorId))
                .filterArray(Criteria.where("o._id").is(authorId))
                .filterArray(Criteria.where("t._id").is(authorId));
        mongoOperations.update(Song.class).apply(update).all();
    }

    @Override
    public void updateRef(Instrument instrument) {
        final var instrumentId = new ObjectId(instrument.getId());
        final var update = new Update()
                .set("instruments.$[i].title", instrument.getTitle())
                .filterArray(Criteria.where("i._id").is(instrumentId));
        mongoOperations.update(Song.class).apply(update).all();
    }

    @Override
    public void updateRef(Vocal vocals) {
        final var vocalId = new ObjectId(vocals.getId());
        final var update = new Update()
                .set("vocals.$[i].title", vocals.getTitle())
                .filterArray(Criteria.where("i._id").is(vocalId));
        mongoOperations.update(Song.class).apply(update).all();
    }

    @Override
    public void updateRef(Topic topic) {
        final var topicId = new ObjectId(topic.getId());
        final var update = new Update()
                .set("topics.$[i].title", topic.getTitle())
                .filterArray(Criteria.where("i._id").is(topicId));
        mongoOperations.update(Song.class).apply(update).all();
    }

    /*
        /*
    db.song.aggregate([
        {
            $project: {
                authors: {
                    $concatArrays: ["$composers", "$arrangers", "$orchestrators", "$translators"],
                }
            }
        },
        {
            $unwind: "$authors"
        },
        {
            $replaceRoot: { newRoot: "$authors" }
        },
        {
            $match: {_id: ObjectId("6197ba7defdde347bcec62b2")}
        }
    ])
     */
    @Override
    public boolean isReferencedBy(Author author) {
        final var aggregationStages = List.of(
                new ProjectionOperation().and("composers").concatArrays("arrangers", "orchestrators", "translators").as("authors"),
                new UnwindOperation(field("authors")),
                new ReplaceRootOperation(field("authors")),
                new MatchOperation(where("_id").is(new ObjectId(author.getId())))
        );
        return mongoOperations.aggregate(newAggregation(aggregationStages), Song.class, Author.class).getMappedResults().size() > 0;
    }

    @Override
    public boolean isReferencedBy(Instrument instrument) {
        final var aggregationStages = List.of(
                new ProjectionOperation(fields("instruments")),
                new UnwindOperation(field("instruments")),
                new ReplaceRootOperation(field("instruments")),
                new MatchOperation(where("_id").is(new ObjectId(instrument.getId())))
        );
        return mongoOperations.aggregate(newAggregation(aggregationStages), Song.class, Instrument.class).getMappedResults().size() > 0;
    }

    @Override
    public boolean isReferencedBy(Vocal vocal) {
        final var aggregationStages = List.of(
                new ProjectionOperation(fields("vocals")),
                new UnwindOperation(field("vocals")),
                new ReplaceRootOperation(field("vocals")),
                new MatchOperation(where("_id").is(new ObjectId(vocal.getId())))
        );
        return mongoOperations.aggregate(newAggregation(aggregationStages), Song.class, Vocal.class).getMappedResults().size() > 0;
    }

    @Override
    public boolean isReferencedBy(Topic topic) {
        final var aggregationStages = List.of(
                new ProjectionOperation(fields("topics")),
                new UnwindOperation(field("topics")),
                new ReplaceRootOperation(field("topics")),
                new MatchOperation(where("_id").is(new ObjectId(topic.getId())))
        );
        return mongoOperations.aggregate(newAggregation(aggregationStages), Song.class, Topic.class).getMappedResults().size() > 0;
    }
}
