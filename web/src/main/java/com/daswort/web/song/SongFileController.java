package com.daswort.web.song;

import com.daswort.core.song.domain.SongFile;
import com.daswort.core.song.domain.SongFileService;
import com.daswort.core.song.query.SongFileQuery;
import com.daswort.core.storage.FileResourceBytes;
import com.daswort.web.http.HttpFileResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.daswort.core.image.transform.ImageTransformationType.LG;


@RestController
@RequestMapping("song/{songCode}/files")
@RequiredArgsConstructor
public class SongFileController {
    private final SongFileService songFileService;

    @GetMapping
    public ResponseEntity<?> getFilesBySongCode(@PathVariable String songCode) {
        return ResponseEntity.ok(songFileService.getSongFiles(songCode));
    }

    @PostMapping("{fileCode}/primary")
    public ResponseEntity<?> makeSongFilePrimary(@PathVariable String songCode,
                                                 @PathVariable String fileCode,
                                                 @RequestParam(value = "isPrimary", required = false, defaultValue = "true") boolean isPrimary) {
        songFileService.makeFilePrimary(songCode, fileCode, isPrimary);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<SongFile> uploadSongFiles(@PathVariable String songCode,
                                                    @RequestParam("file") MultipartFile file) throws IOException {
        final var savedFile = songFileService.addSongFile(songCode, file.getOriginalFilename(), new FileResourceBytes(file.getBytes(), MediaType.APPLICATION_OCTET_STREAM_VALUE));
        return ResponseEntity.of(savedFile);
    }

    @GetMapping("{fileCode}")
    public ResponseEntity<SongFile> getSongFile(@PathVariable String songCode,
                                                @PathVariable String fileCode) {
        return ResponseEntity.of(songFileService.getSongFile(new SongFileQuery(songCode, fileCode)));
    }

    @GetMapping("download/{fileCode}/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String songCode, @PathVariable String fileCode, @PathVariable String fileName) {
        return songFileService.getSongFileResource(new SongFileQuery(songCode, fileCode))
                .map(HttpFileResponseType::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("{fileCode}")
    public ResponseEntity<?> deleteSongFile(@PathVariable String songCode, @PathVariable String fileCode) {
        songFileService.deleteSongFile(new SongFileQuery(songCode, fileCode));
        return ResponseEntity.ok().build();
    }

    @PostMapping("{fileCode}/thumbs")
    public ResponseEntity<?> createSongFileThumbnails(@PathVariable String songCode, @PathVariable String fileCode) {
        songFileService.createFileThumbnail(new SongFileQuery(songCode, fileCode), LG);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{fileCode}/thumbs/{thumbCode}")
    public ResponseEntity<?> downloadFileThumbnail(@PathVariable String songCode,
                                                   @PathVariable String fileCode,
                                                   @PathVariable String thumbCode) {
        return songFileService.getSongFileThumb(new SongFileQuery(songCode, fileCode), thumbCode)
                .map(HttpFileResponseType::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("{fileCode}/thumbs/{thumbCode}")
    public ResponseEntity<?> deleteFileThumbnail(@PathVariable String songCode, @PathVariable String fileCode, @PathVariable String thumbCode) {
        songFileService.deleteSongFileThumb(new SongFileQuery(songCode, fileCode), thumbCode);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("{fileCode}/thumbs}")
    public ResponseEntity<?> deleteAllFileThumbnails(@PathVariable String songCode, @PathVariable String fileCode) {
        songFileService.deleteSongAllFileThumbs(new SongFileQuery(songCode, fileCode));
        return ResponseEntity.ok().build();

    }
}
