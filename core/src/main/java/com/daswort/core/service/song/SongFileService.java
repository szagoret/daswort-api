package com.daswort.core.service.song;

import com.daswort.core.service.storage.FileStorageService;
import com.daswort.core.service.storage.SongFileLocationResolver;
import com.daswort.core.storage.FileResource;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.daswort.core.service.storage.SongFileLocationResolver.generateFileCode;
import static java.util.Objects.requireNonNull;

@Service
public class SongFileService {

    private final FileStorageService fileStorageService;
    private final SongFileLocationResolver songFileLocationResolver;

    public SongFileService(FileStorageService fileStorageService,
                           SongFileLocationResolver songFileLocationResolver) {
        this.fileStorageService = fileStorageService;
        this.songFileLocationResolver = songFileLocationResolver;
    }


    public String saveSongFile(String songId, FileResource fileResource) {
        final var fileCode = generateFileCode();
        fileStorageService.put(songFileLocationResolver.apply(songId, fileCode), fileResource);
        return fileCode;
    }


    public Optional<FileResource> getSongFile(String songId, String fileCode) {
        requireNonNull(songId, fileCode);
        return fileStorageService.get(songFileLocationResolver.apply(songId, fileCode));
    }

    public void removeSongFile(String songId, String fileCode) {
        requireNonNull(songId, fileCode);
        fileStorageService.delete(songFileLocationResolver.apply(songId, fileCode));
    }
}
