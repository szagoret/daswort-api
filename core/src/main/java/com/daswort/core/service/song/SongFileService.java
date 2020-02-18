package com.daswort.core.service.song;

import com.daswort.core.service.storage.FileStorageService;
import com.daswort.core.service.storage.SongFileLocationResolver;
import com.daswort.core.storage.FileResource;
import com.daswort.core.storage.FileResourceBytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.Optional;

import static com.daswort.core.service.storage.SongFileLocationResolver.generateFileCode;
import static java.util.Objects.requireNonNull;

@Service
public class SongFileService {

    private final FileStorageService fileStorageService;
    private final SongFileLocationResolver songFileLocationResolver;


    @Value("classpath:pdf-file.pdf")
    private Resource resourceFile;

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
        try {
            return Optional.of(new FileResourceBytes(resourceFile.getInputStream().readAllBytes(), "pdf-file.pdf", MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
//        return fileStorageService.get(songFileLocationResolver.apply(songId, fileCode));
    }

    public void removeSongFile(String songId, String fileCode) {
        requireNonNull(songId, fileCode);
        fileStorageService.delete(songFileLocationResolver.apply(songId, fileCode));
    }
}
