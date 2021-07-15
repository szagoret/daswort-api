package com.daswort.web.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
public class AppPingScheduler {

    private final RestTemplate restTemplate;
    private final Environment env;

    public AppPingScheduler(RestTemplate restTemplate,
                            Environment env) {
        this.restTemplate = restTemplate;
        this.env = env;
    }


//    @Scheduled(fixedRate = 60 * 1000 * 25)
    public void ping() {
        restTemplate.getForEntity(Objects.requireNonNull(env.getProperty("daswort.ping.endpoint")), Object.class);
    }
}
