package com.daswort.web.http;

import com.daswort.core.song.domain.ResourceEntity;
import com.daswort.core.storage.EntityFileResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class HttpFileResponseType {

    public static ResponseEntity<?> ok(EntityFileResource<? extends ResourceEntity> entityFileResource) {
        final var fileName = Optional.ofNullable(entityFileResource.getEntity().getName()).or(() -> ofNullable(entityFileResource.getEntity().getCode())).orElse(Instant.now().toString());
        final var byteArray = entityFileResource.getBytes();
        final var mediaType = resolveMediaType(entityFileResource.getEntity());

        final var contentDispositionHeader = ContentDispositionBuilder.builder().filename(fileName).build().toString();
        final var contentLengthHeader = byteArray.length;
        final var contentTypeHeader = mediaType.toString();

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(byteArray.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDispositionHeader)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLengthHeader))
                .header(HttpHeaders.CONTENT_TYPE, contentTypeHeader)
                .body(new ByteArrayResource(byteArray));
    }

    private static MediaType resolveMediaType(ResourceEntity resourceEntity) {
        final var extension = resourceEntity.getExtension();
        if ("pdf".equals(extension)) {
            return MediaType.APPLICATION_PDF;
        } else if (List.of("jpg", "jpeg").contains(extension)) {
            return MediaType.IMAGE_JPEG;
        } else if ("png".equals(extension)) {
            return MediaType.IMAGE_PNG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
