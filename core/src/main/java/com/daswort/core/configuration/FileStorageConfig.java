package com.daswort.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.ContainerCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.Objects;

@Configuration
public class FileStorageConfig {

    private final Environment env;

    public FileStorageConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @Profile("!local")
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(env.getProperty("daswort.aws.region", "eu-central-1")))
                .credentialsProvider(ContainerCredentialsProvider.builder().build()).build();
    }

    @Bean
    @Profile("local")
    public S3Client localS3Client() {
        return S3Client.builder()
                .region(Region.of(env.getProperty("daswort.aws.region", "eu-central-1")))
                .endpointOverride(URI.create(Objects.requireNonNull(env.getProperty("daswort.aws.s3.endpoint")))).build();
    }


}
