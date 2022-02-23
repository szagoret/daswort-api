package com.daswort.core.song.domain;

import com.daswort.core.common.entity.SequenceGenerator;
import com.daswort.core.image.transform.ImageTransformationType;
import com.daswort.core.service.storage.FileStorageService;
import com.daswort.core.service.storage.SongFilePathBuilder;
import com.daswort.core.service.storage.SongFilePathResolver;
import com.daswort.core.song.application.SongFileThumbnailService;
import com.daswort.core.song.application.ThumbnailCodeBuilder;
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
import java.util.stream.IntStream;

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

    public Optional<SongFile> addSongFile(String songCode, String fileName, FileResource fileResource) {
        requireNonNull(songCode);
        requireNonNull(fileResource);
        final var fileExtension = FileUtils.getFileExtension(fileName);
        final var sequence = sequenceGenerator.nextSequence(SongFile.SEQUENCE);
        final var file = SongFile.builder()
                .name(fileName)
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

    public void deleteSongFileResources(String songCode, SongFile file) {
        fileStorageService.delete(SongFilePathResolver.resolve(songCode, file));
        file.getThumbnails().forEach((thumbnail) -> deleteSongFileThumbResource(new SongFileQuery(songCode, file.getCode()), thumbnail));
        songFileRepository.deleteSongFile(songCode, file.getCode());
    }

    public void deleteSongFile(SongFileQuery query) {
        songFileRepository.getSongFile(query.songCode(), query.fileCode()).ifPresent(file -> deleteSongFileResources(query.songCode(), file));
    }

    public void createFileThumbnail(SongFileQuery query, ImageTransformationType... imageTransformationTypes) {
        getSongFileResource(query).ifPresent(songFileResource -> {
            for (var transformationType : imageTransformationTypes) {
                final var fileResources = songFileThumbnailService.createFileThumbnails(songFileResource, transformationType);
                IntStream.range(0, fileResources.size()).forEach(index -> {
                    final var thumbnailCode = ThumbnailCodeBuilder.build(query.fileCode(), transformationType.name().toLowerCase(), index, transformationType.getImageTransformer().getFileFormat());
                    final var fileResource = fileResources.get(index);
                    final var thumbnail = Thumbnail.builder()
                            .code(thumbnailCode)
                            .type(transformationType.name())
                            .size(fileResource.getContentLength())
                            .extension(transformationType.getImageTransformer().getFileFormat().name())
                            .sequence(index)
                            .build();
                    final var thumbnailPath = SongFilePathBuilder.build(query.songCode(), query.fileCode(), thumbnailCode);

                    songFileThumbnailRepository.save(query.songCode(), query.fileCode(), thumbnail)
                            .ifPresent(t -> fileStorageService.put(thumbnailPath, fileResource));
                });
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

    public void deleteSongFileThumbResource(SongFileQuery query, Thumbnail thumbnail) {
        fileStorageService.delete(SongFilePathResolver.resolve(query, thumbnail));
    }

    public void deleteSongFileThumb(SongFileQuery query, Thumbnail thumbnail) {
        deleteSongFileThumbResource(query, thumbnail);
        songFileThumbnailRepository.deleteThumbnail(query.songCode(), query.fileCode(), thumbnail.getCode());
    }

    public void deleteSongFileThumb(SongFileQuery query, String thumbCode) {
        songFileThumbnailRepository.findByCode(query.songCode(), query.fileCode(), thumbCode).ifPresent(thumbnail -> deleteSongFileThumb(query, thumbnail));
    }

    public void deleteSongAllFileThumbs(SongFileQuery query) {
        songFileRepository.getSongFile(query.songCode(), query.fileCode())
                .map(SongFile::getThumbnails)
                .orElse(List.of()).forEach(thumbnail -> deleteSongFileThumb(query, thumbnail));
    }
}
