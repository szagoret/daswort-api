package com.daswort.core.song.application;

import com.daswort.core.SequenceGenerator;
import com.daswort.core.image.transform.ImageTransformationType;
import com.daswort.core.service.storage.FileStorageService;
import com.daswort.core.service.storage.SongFilePathBuilder;
import com.daswort.core.song.domain.SongFile;
import com.daswort.core.song.domain.Thumbnail;
import com.daswort.core.song.query.SongFileQuery;
import com.daswort.core.song.repository.SongFileRepository;
import com.daswort.core.song.repository.SongFileThumbnailRepository;
import com.daswort.core.storage.FileResource;
import com.daswort.core.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class SongFileService {

    private final FileStorageService fileStorageService;
    private final SequenceGenerator sequenceGenerator;
    private final SongFileRepository songFileRepository;
    private final SongFileThumbnailService songFileThumbnailService;
    private final SongFileThumbnailRepository songFileThumbnailRepository;

    public List<SongFile> getSongFiles(String songCode) {
        return songFileRepository.getSongFiles(songCode);
    }

    public Optional<SongFile> getSongFile(SongFileQuery query) {
        return songFileRepository.getSongFile(query.songCode(), query.fileCode());
    }

    public Optional<SongFile> addSongFile(String songCode, FileResource fileResource) {
        requireNonNull(songCode);
        requireNonNull(fileResource);
        final var fileExtension = FileUtils.getFileExtension(fileResource.getName());
        final var sequence = sequenceGenerator.nextSequence(SongFile.SEQUENCE);
        final var file = SongFile.builder()
                .name(fileResource.getName())
                .code(sequence)
                .extension(fileExtension.orElse(""))
                .size(fileResource.getContentLength())
                .uploadedAt(Instant.now())
                .build();

        final String songFilePath = SongFilePathBuilder.build(songCode, sequence);
        final var savedSong = songFileRepository.saveSongFile(songCode, file);
        savedSong.ifPresent(savedFile -> fileStorageService.put(songFilePath, fileResource));
        return savedSong;
    }


    public Optional<SongFileResource> getSongFileResource(SongFileQuery query) {
        final var songCode = query.songCode();
        final var fileCode = query.fileCode();
        final Optional<SongFile> songFileOpt = songFileRepository.getSongFile(songCode, fileCode);
        if (songFileOpt.isEmpty()) {
            return Optional.empty();
        }
        final SongFile songFile = songFileOpt.get();
        final var filePath = SongFilePathBuilder.build(songCode, fileCode);

        return fileStorageService.get(filePath).map(fileResource -> new SongFileResource(fileResource, songFile));
    }

    public void makeFilePrimary(String songCode, String fileCode, boolean isPrimary) {
        songFileRepository.makeFilePrimary(songCode, fileCode, isPrimary);
    }

    public void removeSongFile(SongFileQuery query) {
        final var songCode = query.songCode();
        final var fileCode = query.fileCode();
        requireNonNull(songCode, fileCode);
        fileStorageService.delete(SongFilePathBuilder.build(songCode, fileCode));
    }

    public void createFileThumbnail(SongFileQuery query, ImageTransformationType... imageTransformationTypes) {
        getSongFileResource(query).ifPresent(songFileResource -> {
            for (var transformationType : imageTransformationTypes) {
                final var fileResource = songFileThumbnailService.createFileThumbnail(songFileResource, transformationType);
                final var thumbnailCode = ThumbnailCodeBuilder.build(query.fileCode(), transformationType.name().toLowerCase(), 0, transformationType.getImageTransformer().getFileFormat());

                final var thumbnail = Thumbnail.builder()
                        .code(thumbnailCode)
                        .type(transformationType.name())
                        .size(fileResource.getContentLength())
                        .extension(transformationType.getImageTransformer().getFileFormat().name())
                        .build();
                final var thumbnailPath = SongFilePathBuilder.build(query.songCode(), query.fileCode(), thumbnailCode);

                songFileThumbnailRepository.save(query.songCode(), query.fileCode(), thumbnail)
                        .ifPresent(t -> fileStorageService.put(thumbnailPath, fileResource));
            }
        });
    }

    public Optional<SongFileThumbnailResource> getSongFileThumb(SongFileQuery query, String thumbCode) {
        final Optional<Thumbnail> thumbOpt = songFileThumbnailRepository.findByCode(query.songCode(), query.fileCode(), thumbCode);
        if (thumbOpt.isEmpty()) {
            return Optional.empty();
        }

        final var thumb = thumbOpt.get();
        final var filePath = SongFilePathBuilder.build(query.songCode(), query.fileCode(), thumbCode);
        return fileStorageService.get(filePath).map(fileResource -> new SongFileThumbnailResource(fileResource, thumb));
    }
}
