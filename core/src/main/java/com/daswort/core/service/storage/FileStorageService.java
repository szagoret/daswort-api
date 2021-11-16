package com.daswort.core.service.storage;

import com.daswort.core.exception.FileStorageException;
import com.daswort.core.storage.FileResource;
import com.daswort.core.storage.FileResourceBytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static software.amazon.awssdk.core.sync.RequestBody.fromBytes;

@Service
public class FileStorageService {

    private final S3Client s3Client;
    private final String bucketName;

    public FileStorageService(S3Client s3Client,
                              @Value("${daswort.aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public Optional<FileResource> get(String location) {
        requireNonNull(location);

        final var getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(location)
                .build();
        try {
            final var responseBytes = s3Client.getObject(getRequest, ResponseTransformer.toBytes());
            final GetObjectResponse res = responseBytes.response();
            final var resource = new FileResourceBytes(responseBytes.asByteArray(), res.contentType());
            return Optional.of(resource);
        } catch (NoSuchKeyException e) {
            return Optional.empty();
        } catch (S3Exception e) {
            throw new FileStorageException(e);
        }
    }

    public void put(String location, FileResource fileResource) {
        requireNonNull(location);
        requireNonNull(fileResource);

        final var putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(location)
                .build();
        try {
            s3Client.putObject(putRequest, fromBytes(fileResource.getBytes()));
        } catch (S3Exception e) {
            throw new FileStorageException(e);
        }
    }

    public void delete(String location) {
        requireNonNull(location);

        final var deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(location)
                .build();

        try {
            s3Client.deleteObject(deleteRequest);
        } catch (S3Exception e) {
            throw new FileStorageException(e);
        }
    }
}
