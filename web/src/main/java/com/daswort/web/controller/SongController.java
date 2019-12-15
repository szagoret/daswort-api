package com.daswort.web.controller;

import com.daswort.core.entity.Song;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.song.SongSearchService;
import com.daswort.web.dto.song.SongSearchSuggestion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/find")
    public List<SongSearchSuggestion> findSongByName(@RequestParam(defaultValue = "") String searchTerm) {
        List<Song> songsByName = songSearchService.findSongsByName(searchTerm);
        return songsByName.stream().map(song -> SongSearchSuggestion.builder()
                .song(song)
                .breadcrumb(buildBreadcrumb(categoryService.computeCategoryTreePath(song.getCategory().getId())))
                .build()
        ).collect(toList());
    }

}
