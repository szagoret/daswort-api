package com.daswort.web.song;

import com.daswort.core.entity.*;
import com.daswort.core.model.SongSearch;
import com.daswort.core.model.SongUpdate;
import com.daswort.core.service.author.AuthorService;
import com.daswort.core.service.category.CategoryService;
import com.daswort.core.service.idname.IdNameService;
import com.daswort.core.service.song.SongFileService;
import com.daswort.core.service.song.SongSearchService;
import com.daswort.core.service.song.SongUpdateService;
import com.daswort.core.service.storage.FileStorageService;
import com.daswort.core.storage.FileResourceBytes;
import com.daswort.web.author.AuthorIdNameDtoMapper;
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
import java.util.Comparator;
import java.util.List;

import static com.daswort.web.idname.IdNameDtoMapper.toIdNameDto;
import static com.daswort.web.song.SongDtoMapper.toSongDto;
import static com.daswort.web.song.SongPathBuilder.buildPath;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.parseMediaType;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io"})
@RestController
@RequestMapping("/song")
@RequiredArgsConstructor
public class SongController {

    private final SongSearchService songSearchService;
    private final SongUpdateService songUpdateService;
    private final CategoryService categoryService;
    private final SongFileService songFileService;
    private final IdNameService idNameService;
    private final AuthorService authorService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{songCode}")
    public ResponseEntity<SongDto> getSongByCode(@PathVariable String songCode) {
        final var song = songSearchService.findSongByCode(songCode);
        return song.map(value -> ResponseEntity.ok(toSongDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{songCode}/files")
    public ResponseEntity<List<File>> getFilesBySongCode(@PathVariable String songCode) {
        final var song = songSearchService.findSongByCode(songCode);
        return song.map(value -> ResponseEntity.ok(value.getFiles().stream().sorted(Comparator.comparing(File::getUploadedAt)).collect(toList()))).orElseGet(() -> ResponseEntity.notFound().build());
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

    @PostMapping("/{songCode}/{fileCode}/thumbnail")
    public ResponseEntity<SongDto> createSongFileThumbnails(@PathVariable String songCode, @PathVariable String fileCode) {
        songUpdateService.createSongFileThumbnails(songCode, fileCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addSong(@RequestBody SongUpdate request) {
        return ResponseEntity.ok(songUpdateService.createSong(request));
    }

    @PutMapping("/{songCode}")
    public ResponseEntity<?> updateSong(@PathVariable String songCode,
                                        @RequestBody SongUpdate request) {
        return ResponseEntity.ok(toSongDto(songUpdateService.updateSong(request, songCode)));
    }

    @PutMapping("/{songCode}/files")
    public ResponseEntity<File> uploadSongFiles(@PathVariable String songCode,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        final var savedFile = songUpdateService.addSongFile(songCode, new FileResourceBytes(file.getBytes(), file.getOriginalFilename(), MediaType.APPLICATION_OCTET_STREAM_VALUE));
        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/{songCode}/files/{fileCode}")
    public ResponseEntity<File> getSongFile(@PathVariable String songCode,
                                            @PathVariable String fileCode) {
        return ResponseEntity.of(songSearchService.getSongFile(songCode, fileCode));
    }

    @GetMapping("/{songCode}/files/{fileCode}/download")
    public ResponseEntity<?> downloadSongFile(@PathVariable String songCode,
                                              @PathVariable String fileCode) {

        return songFileService.getFileResource(songCode, fileCode)
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

    @GetMapping(value = "/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getSongFilePreview(@RequestParam String path) {
        return fileStorageService.get(path)
                .map(fileResource ->
                        ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .contentLength(fileResource.getContentLength())
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                        ContentDispositionBuilder.builder()
                                                .filename(path.replace("/", "_"))
                                                .build()
                                                .toString())
                                .body(new InputStreamResource(fileResource.getInputStream())))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{songCode}")
    public ResponseEntity<?> deleteSong(@PathVariable String songCode) {
        songUpdateService.removeSong(songCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{songCode}/files/{fileCode}")
    public ResponseEntity<?> deleteSongFile(@PathVariable String songCode,
                                            @PathVariable String fileCode) {
        return ResponseEntity.ok(songUpdateService.removeSongFile(songCode, fileCode));
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
