package com.daswort.core.service.song;

import com.daswort.core.entity.Song;
import com.daswort.core.service.storage.FileStorageService;
import com.daswort.core.service.storage.SongFilePathBuilder;
import com.daswort.core.service.storage.SongFileThumbnailPathBuilder;
import com.daswort.core.storage.FileResource;
import com.daswort.core.storage.FileResourceBytes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
public class SongFileService {

    private final FileStorageService fileStorageService;
    private final SongFilePathBuilder songFilePathBuilder;
    private final SongFileThumbnailPathBuilder songFileThumbnailPathBuilder;
    private final MongoOperations mongoOperations;
    private final SongSearchService songSearchService;
    private final SongThumbnailService songThumbnailService;

    public String saveSongFile(String songCode, FileResource fileResource, String sequence, String fileExtension) {
        final var extension = Optional.ofNullable(fileExtension)
                .filter(e -> e.length() > 0)
                .map(e -> String.format(".%s", e)).orElse("");
        final var filePath = songFilePathBuilder.build(songCode, String.format("%s%s", sequence, extension));
        fileStorageService.put(filePath, fileResource);
        return filePath;
    }

    public void createFileThumbnail(String songCode, String originalFileCode) {
        getFileResource(songCode, originalFileCode).ifPresent(fileResource ->
                List.of(ThumbnailType.SM, ThumbnailType.LG).forEach(thumbnailType -> {
                    final Integer pageIndex = 0;
                    final var byteArrayOutputStream = songThumbnailService.createSongThumbnail(fileResource, pageIndex, thumbnailType);
                    final var fileResourceBytes = new FileResourceBytes(byteArrayOutputStream.toByteArray(), originalFileCode, "application/octet-stream");
                    saveSongThumbnail(songCode, originalFileCode, fileResourceBytes, thumbnailType, pageIndex);
                }));
    }

    public void saveSongThumbnail(String songCode,
                                  String originalFileCode,
                                  FileResource fileResource,
                                  ThumbnailType thumbnailType,
                                  Integer page) {
        Optional.ofNullable(songSearchService.getSongFile(songCode, originalFileCode)).ifPresent(file -> {
            final var thumbnailFilePath = songFileThumbnailPathBuilder.build(songCode, originalFileCode, thumbnailType, page);
            fileStorageService.put(thumbnailFilePath, fileResource);

            final var thumbnailFieldType = thumbnailType.equals(ThumbnailType.LG) ? "lgThumbnails" : "smThumbnails";
            final var query = new Query(where("code").is(songCode));
            final var update = new Update()
                    .addToSet("files.$[e]." + thumbnailFieldType, thumbnailFilePath)
                    .filterArray("e.code", originalFileCode);

            mongoOperations.update(Song.class)
                    .matching(query)
                    .apply(update)
                    .first();
        });
    }


    public Optional<FileResource> getFileResource(String songCode, String fileCode) {
        requireNonNull(songCode, fileCode);
        return fileStorageService.get(songFilePathBuilder.build(songCode, fileCode));
    }

    public void removeSongFile(String filePath) {
        requireNonNull(filePath);
        fileStorageService.delete(filePath);
    }

    public void removeSongFile(String songCode, String fileCode) {
        requireNonNull(songCode, fileCode);
        fileStorageService.delete(songFilePathBuilder.build(songCode, fileCode));
    }
}
