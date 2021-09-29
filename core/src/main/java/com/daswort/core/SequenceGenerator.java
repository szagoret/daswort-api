package com.daswort.core;

import com.daswort.core.entity.Sequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SequenceGenerator {
    private final MongoOperations operations;

    public String nextSequence(EntitySequenceName sequenceName) {
        final Query q = new Query(Criteria.where("id").is(sequenceName.name()));

        final Update u = new Update().inc("value", 1);
        final Sequence sequence = operations.findAndModify(q, u, FindAndModifyOptions.options().returnNew(true).upsert(true), Sequence.class);
        return buildSequence(sequence);
    }

    private String buildSequence(Sequence sequence) {
        final var sequencePrefix = "noav";
        return String.format("%s-%d", sequencePrefix, !Objects.isNull(sequence) ? sequence.getValue() : 10000);
    }
}
