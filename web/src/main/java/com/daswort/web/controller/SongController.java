package com.daswort.web.controller;

import com.daswort.core.entity.IdName;
import com.daswort.core.entity.Song;
import com.daswort.core.service.IdNameCollection;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.song.SongSearchService;
import com.daswort.web.dto.song.EditSongRequest;
import com.daswort.web.dto.song.SongSearchSuggestion;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.daswort.web.dto.breadcrumb.BreadcrumbBuilder.buildBreadcrumb;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/song")
public class SongController {

    private final SongSearchService songSearchService;
    private final CategoryService categoryService;

    public SongController(SongSearchService songSearchService,
                          CategoryService categoryService) {
        this.songSearchService = songSearchService;
        this.categoryService = categoryService;
    }

    @GetMapping("{songId}")
    public Song getSongById(@PathVariable String songId) {
        return songSearchService.findSongById(songId).orElse(Song.builder().build());
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
    public Song addSong(@RequestBody EditSongRequest request) {
        return Song.builder().build();
    }

    @PutMapping("/{songId{")
    public Song updateSong(@PathVariable String songId,
                           @RequestBody EditSongRequest request) {
        return Song.builder().build();
    }

    @PutMapping("/{songId}/files")
    public Song uploadSongFiles(@PathVariable String songId,
                                @RequestParam("files") MultipartFile[] files) {
        return Song.builder().build();
    }

    @GetMapping("{collection}")
    public IdName getIdNameById(@PathVariable IdNameCollection collection) {
        return IdName.builder().build();
    }


}
