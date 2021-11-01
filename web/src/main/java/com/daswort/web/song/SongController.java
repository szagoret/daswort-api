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
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
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

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io", "http://192.168.1.38:3000", "http://192.168.1.38:3000/daswort-ui"})
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
        final var songDto = songSearchService.findSongByCode(songCode).map(SongDtoMapper::toSongDto);
        return songDto.map(value -> ResponseEntity.ok(value.getFiles().stream().sorted(Comparator.comparing(File::getUploadedAt)).collect(toList()))).orElseGet(() -> ResponseEntity.notFound().build());
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

    @PostMapping("/{songCode}/{fileCode}/primary")
    public ResponseEntity<?> makeSongFilePrimary(@PathVariable String songCode,
                                                 @PathVariable String fileCode,
                                                 @RequestParam(value = "isPrimary", required = false, defaultValue = "true")
                                                         boolean isPrimary) {
        songUpdateService.makeFilePrimary(songCode, fileCode, isPrimary);
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

    @GetMapping("/{songCode}/files/download/{fileName}")
    public ResponseEntity<?> downloadSongFile(@PathVariable String songCode,
                                              @PathVariable String fileName) {

        return songFileService.getSongFileResource(songCode, fileName)
                .map(fileResource -> {
                    byte[] s;
                    try {
                        s = IOUtils.toByteArray(fileResource.getFileResource().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        s = new byte[0];
                    }
                    return ResponseEntity.ok()
//                            .contentType(parseMediaType(fileResource.getContentType()))
                            .contentType(MediaType.APPLICATION_PDF)
                            .contentLength(fileResource.getFileResource().getContentLength())
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    ContentDispositionBuilder.builder()
                                            .filename(fileResource.getFile().getName())
                                            .build()
                                            .toString())
                            .body(new ByteArrayResource(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getSongFilePreview(@RequestParam String path) {
        return fileStorageService.get(path)
                .map(fileResource -> {
                    byte[] s;
                    try {
                        s = IOUtils.toByteArray(fileResource.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        s = new byte[0];
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .contentLength(fileResource.getContentLength())
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    ContentDispositionBuilder.builder()
                                            .filename(path.replace("/", "_"))
                                            .build()
                                            .toString())
                            .body(s);
                }).orElse(ResponseEntity.notFound().build());
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
