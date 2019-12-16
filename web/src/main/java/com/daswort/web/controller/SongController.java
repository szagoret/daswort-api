package com.daswort.web.controller;

import com.daswort.core.entity.Song;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.song.SongSearchService;
import com.daswort.core.service.song.SongUpdate;
import com.daswort.core.service.song.SongUpdateService;
import com.daswort.web.dto.song.SongSearchSuggestion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.daswort.web.dto.breadcrumb.BreadcrumbBuilder.buildBreadcrumb;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/song")
public class SongController {

    private final SongSearchService songSearchService;
    private final SongUpdateService songUpdateService;
    private final CategoryService categoryService;

    public SongController(SongSearchService songSearchService,
                          SongUpdateService songUpdateService,
                          CategoryService categoryService) {
        this.songSearchService = songSearchService;
        this.songUpdateService = songUpdateService;
        this.categoryService = categoryService;
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
    public ResponseEntity<?> uploadSongFiles(@PathVariable String songId,
                                             @RequestParam("files") MultipartFile[] files) {
        return ResponseEntity.ok(new Song());
    }

}
