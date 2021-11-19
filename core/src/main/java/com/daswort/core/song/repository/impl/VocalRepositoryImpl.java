package com.daswort.core.song.repository.impl;

import com.daswort.core.common.entity.AbstractIdTitleEntityRepository;
import com.daswort.core.song.domain.Vocal;
import com.daswort.core.song.repository.VocalRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class VocalRepositoryImpl extends AbstractIdTitleEntityRepository<Vocal> implements VocalRepository {

    public VocalRepositoryImpl(MongoOperations operations) {
        super(operations);
    }
}
