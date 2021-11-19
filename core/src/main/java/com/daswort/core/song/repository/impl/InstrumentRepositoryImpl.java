package com.daswort.core.song.repository.impl;

import com.daswort.core.common.entity.AbstractIdTitleEntityRepository;
import com.daswort.core.song.domain.Instrument;
import com.daswort.core.song.repository.InstrumentRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class InstrumentRepositoryImpl extends AbstractIdTitleEntityRepository<Instrument> implements InstrumentRepository {

    public InstrumentRepositoryImpl(MongoOperations operations) {
        super(operations);
    }
}
