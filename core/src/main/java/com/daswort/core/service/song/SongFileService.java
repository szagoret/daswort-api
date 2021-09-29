package com.daswort.core.service.song;

import com.daswort.core.entity.Song;
import com.daswort.core.service.storage.FileStorageService;
import com.daswort.core.service.storage.SongFileLocationResolver;
import com.daswort.core.service.storage.SongFileThumbnailLocationResolver;
import com.daswort.core.storage.FileResource;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.daswort.core.service.storage.SongFileLocationResolver.generateFileCode;
import static java.util.Objects.requireNonNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
public class SongFileService {

    private final FileStorageService fileStorageService;
    private final SongFileLocationResolver songFileLocationResolver;
    private final SongFileThumbnailLocationResolver songFileThumbnailLocationResolver;
    private final MongoOperations mongoOperations;

    public String saveSongFile(String songCode, FileResource fileResource) {
        final var fileCode = generateFileCode();
        fileStorageService.put(songFileLocationResolver.apply(songCode, fileCode), fileResource);
        return fileCode;
    }

    public void saveSongThumbnail(String songCode, String parentSongCode, FileResource fileResource) {
        final var fileCode = generateFileCode();
        final var thumbnailFile = songFileThumbnailLocationResolver.apply(songCode, parentSongCode, fileCode);
        fileStorageService.put(thumbnailFile, fileResource);

        final var query = new Query(where("code").is(new ObjectId(songCode)));
        final var update = new Update()
                .addToSet("files.$[e].thumbnails", fileCode)
                .filterArray("e.fileCode", parentSongCode);

        mongoOperations.update(Song.class)
                .matching(query)
                .apply(update)
                .first();
    }


    public Optional<FileResource> getSongFile(String songCode, String fileCode) {
        requireNonNull(songCode, fileCode);
        return fileStorageService.get(songFileLocationResolver.apply(songCode, fileCode));
    }

    public void removeSongFile(String songCode, String fileCode) {
        requireNonNull(songCode, fileCode);
        fileStorageService.delete(songFileLocationResolver.apply(songCode, fileCode));
    }
}
