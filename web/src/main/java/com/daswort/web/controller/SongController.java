package com.daswort.web.controller;

import com.daswort.core.entity.File;
import com.daswort.core.entity.Song;
import com.daswort.core.model.SongUpdate;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.song.SongFileService;
import com.daswort.core.service.song.SongSearchService;
import com.daswort.core.service.song.SongUpdateService;
import com.daswort.core.storage.FileResourceBytes;
import com.daswort.web.dto.song.SongSearchSuggestion;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.daswort.web.dto.breadcrumb.BreadcrumbBuilder.buildBreadcrumb;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.parseMediaType;

@RestController
@RequestMapping("/song")
public class SongController {

    private final SongSearchService songSearchService;
    private final SongUpdateService songUpdateService;
    private final CategoryService categoryService;
    private final SongFileService songFileService;

    public SongController(SongSearchService songSearchService,
                          SongUpdateService songUpdateService,
                          CategoryService categoryService,
                          SongFileService songFileService) {
        this.songSearchService = songSearchService;
        this.songUpdateService = songUpdateService;
        this.categoryService = categoryService;
        this.songFileService = songFileService;
    }

    @GetMapping("/{songId}")
    public Song getSongById(@PathVariable String songId) {
        return songSearchService.findSongById(songId).orElse(new Song());
    }

    @GetMapping("/find")
    public List<SongSearchSuggestion> findSongsByName(@RequestParam(defaultValue = "") String searchTerm) {
        List<Song> songsByName = songSearchService.findSongsByName(searchTerm);
        return songsByName.stream().map(song -> SongSearchSuggestion.builder()
                .song(song)
                .breadcrumb(buildBreadcrumb(categoryService.computeCategoryTreePath(song.getCategory().getId())))
                .build()
        ).collect(toList());
    }

    @PostMapping
    public ResponseEntity<?> addSong(@RequestBody SongUpdate request) {
        return ResponseEntity.ok(songUpdateService.createSong(request));
    }

    @PutMapping("/{songId}")
    public ResponseEntity<?> updateSong(@PathVariable String songId,
                                        @RequestBody SongUpdate request) {
        return ResponseEntity.ok(songUpdateService.updateSong(request, songId));
    }

    @PutMapping("/{songId}/files")
    public ResponseEntity<File> uploadSongFiles(@PathVariable String songId,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        final var savedFile = songUpdateService.addSongFile(songId, new FileResourceBytes(file.getBytes(), file.getOriginalFilename(), MediaType.APPLICATION_OCTET_STREAM_VALUE));
        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/{songId}/files/{fileCode}")
    public ResponseEntity<File> getSongFile(@PathVariable String songId,
                                            @PathVariable String fileCode) {
        final var file = songSearchService.getSongFile(songId, fileCode);
        return ResponseEntity.ok(file);
    }

    @GetMapping("/{songId}/files/{fileCode}/download")
    public ResponseEntity<?> downloadSongFile(@PathVariable String songId,
                                              @PathVariable String fileCode) {

        final var file = songSearchService.getSongFile(songId, fileCode);

        return songFileService.getSongFile(songId, fileCode)
                .map(fileResource ->
                        ResponseEntity.ok()
                                .contentType(parseMediaType(fileResource.getContentType()))
                                .contentLength(fileResource.getContentLength())
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                        ContentDispositionBuilder.builder()
                                                .filename(file.getName())
                                                .build()
                                                .toString())
                                .body(new InputStreamResource(fileResource.getInputStream())))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<?> deleteSong(@PathVariable String songId) {
        songUpdateService.removeSong(songId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{songId}/files/{fileCode}")
    public ResponseEntity<?> deleteSongFile(@PathVariable String songId,
                                            @PathVariable String fileCode) {
        songUpdateService.removeSongFile(songId, fileCode);
        return ResponseEntity.ok().build();
    }

}
