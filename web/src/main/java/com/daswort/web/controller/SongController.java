package com.daswort.web.controller;

import com.daswort.core.entity.*;
import com.daswort.core.model.SongSearch;
import com.daswort.core.model.SongUpdate;
import com.daswort.core.pdf.SongPdfImageCreator;
import com.daswort.core.service.author.AuthorService;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.idname.IdNameService;
import com.daswort.core.service.song.SongFileService;
import com.daswort.core.service.song.SongSearchService;
import com.daswort.core.service.song.SongUpdateService;
import com.daswort.core.storage.FileResourceBytes;
import com.daswort.web.dto.song.SongDto;
import com.daswort.web.dto.song.SongFiltersDto;
import com.daswort.web.dto.song.SongPageableListDto;
import com.daswort.web.dto.song.SongSearchSuggestion;
import com.daswort.web.mapper.AuthorIdNameDtoMapper;
import com.daswort.web.mapper.SongDtoMapper;
import com.daswort.web.util.ContentDispositionBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.daswort.web.builder.SongPathBuilder.buildPath;
import static com.daswort.web.mapper.IdNameDtoMapper.toIdNameDto;
import static com.daswort.web.mapper.SongDtoMapper.toSongDto;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.parseMediaType;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io"})
@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongPdfImageCreator songPdfImageCreator;
    private final SongSearchService songSearchService;
    private final SongUpdateService songUpdateService;
    private final CategoryService categoryService;
    private final SongFileService songFileService;
    private final IdNameService idNameService;
    private final AuthorService authorService;

    @GetMapping("/{songId}")
    public ResponseEntity<SongDto> getSongById(@PathVariable String songId) {
        final var song = songSearchService.findSongById(songId).orElse(new Song());
        return ResponseEntity.ok(toSongDto(song));
    }

    @PostMapping("/{songId}/{fileCode}/preview")
    public ResponseEntity<SongDto> createFilePreview(@PathVariable String songId, @PathVariable String fileCode) {
        songPdfImageCreator.createPdfPreview(songId, fileCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find")
    public List<SongSearchSuggestion> findSongsByName(@RequestParam(defaultValue = "") String searchTerm) {
        List<Song> songsByName = songSearchService.findSongsByName(searchTerm);
        return songsByName.stream()
                .map(song -> SongSearchSuggestion.builder()
                        .song(toSongDto(song))
                        .path(buildPath(categoryService.getCategoryParentTreePath(ofNullable(song.getCategory()).map(Category::getId).orElse(null))))
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
        return ResponseEntity.ok(toSongDto(songUpdateService.updateSong(request, songId)));
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
                                                .filename(fileResource.getName())
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
        return ResponseEntity.ok(songUpdateService.removeSongFile(songId, fileCode));
    }

    @PostMapping("/search")
    public ResponseEntity<SongPageableListDto> advancedSearch(@RequestBody @Validated SongSearch songSearch) {
        final var songSearchResult = songSearchService.advancedSearch(songSearch, PageRequest.of(songSearch.getPage(), songSearch.getSize()));
        final var songsDtos = songSearchResult.getSongList().stream().map(SongDtoMapper::toSongDto).collect(toList());
        var result = SongPageableListDto.builder()
                .songs(songsDtos)
                .page(songSearchResult.getPageable().getPageNumber())
                .size(songSearchResult.getPageable().getPageSize())
                .total(songSearchResult.getTotalCount())
                .build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filters")
    public ResponseEntity<SongFiltersDto> getFilters() {
        List<IdName> topics = idNameService.getAll(IdNameCollection.topic);
        List<IdName> compositions = idNameService.getAll(IdNameCollection.composition);
        List<IdName> difficulties = idNameService.getAll(IdNameCollection.difficulty);
        List<IdName> instruments = idNameService.getAll(IdNameCollection.instrument);
        List<Author> authors = authorService.getAuthorList();
        final var songFilters = SongFiltersDto.builder()
                .topics(toIdNameDto(topics))
                .compositions(toIdNameDto(compositions))
                .difficulties(toIdNameDto(difficulties))
                .instruments(toIdNameDto(instruments))
                .authors(AuthorIdNameDtoMapper.toIdNameDto(authors))
                .melodies(AuthorIdNameDtoMapper.toIdNameDto(authors))
                .arrangements(AuthorIdNameDtoMapper.toIdNameDto(authors))
                .adaptations(AuthorIdNameDtoMapper.toIdNameDto(authors))
                .build();

        return ResponseEntity.ok(songFilters);
    }

}
