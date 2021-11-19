package com.daswort.core.song.repository;

import com.daswort.core.song.domain.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends CrudRepository<Topic, String> {
}
