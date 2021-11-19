package com.daswort.core.song.repository.impl;

import com.daswort.core.common.entity.AbstractIdTitleEntityRepository;
import com.daswort.core.song.domain.Instrument;
import com.daswort.core.song.domain.Topic;
import com.daswort.core.song.repository.InstrumentRepository;
import com.daswort.core.song.repository.TopicRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class TopicRepositoryImpl extends AbstractIdTitleEntityRepository<Topic> implements TopicRepository {

    public TopicRepositoryImpl(MongoOperations operations) {
        super(operations);
    }
}
