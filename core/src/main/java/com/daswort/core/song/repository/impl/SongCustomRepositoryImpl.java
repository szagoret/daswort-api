package com.daswort.core.song.repository.impl;

import com.daswort.core.song.domain.Author;
import com.daswort.core.song.domain.Song;
import com.daswort.core.song.repository.SongCustomRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void updateAuthor(Author author) {
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

        mongoOperations.update(Song.class)
                .apply(update)
                .all();
    }
}
