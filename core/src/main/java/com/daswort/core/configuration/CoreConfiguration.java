package com.daswort.core.configuration;

import com.daswort.core.entity.Song;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;

@Slf4j
@Configuration
public class CoreConfiguration {

    private final MongoOperations mongoOperations;

    public CoreConfiguration(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Bean
    public CommandLineRunner commandLineAppStartupRunner() {
        return args -> mongoOperations.indexOps(Song.class).ensureIndex(new Index().on("name", Sort.Direction.ASC));
    }
}
