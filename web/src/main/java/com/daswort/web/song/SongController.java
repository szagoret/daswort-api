package com.daswort.web.song;

import com.daswort.core.song.application.SongService;
import com.daswort.core.song.domain.Song;
import com.daswort.core.song.query.SongSearchQuery;
import com.daswort.web.song.dto.SaveSongCommandMapper;
import com.daswort.web.song.dto.SongDto;
import com.daswort.web.song.dto.SongDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.daswort.web.song.dto.SongDtoMapper.toSongDto;
import static java.util.stream.Collectors.toList;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io", "http://192.168.1.38:3000", "http://192.168.1.38:3000/daswort-ui"})
@RestController
@RequestMapping("song")
@RequiredArgsConstructor
public class SongController {
    private final SongService songService;

    @GetMapping("/{songCode}")
    public ResponseEntity<SongDto> getByCode(@PathVariable String songCode) {
        final var song = songService.findByCode(songCode);
        return ResponseEntity.of(song.flatMap(SongDtoMapper::toSongDto));
    }

    @GetMapping("/find")
    public List<SongSearchSuggestion> findByName(@RequestParam(defaultValue = "") String searchTerm) {
        List<Song> songsByName = songService.findByName(searchTerm);
        return songsByName.stream().map(song -> SongSearchSuggestion.builder().song(toSongDto(song).orElseThrow()).build()).collect(toList());
    }

    @GetMapping("/search")
    public ResponseEntity<?> advancedSearch(SongSearchQuery songSearchQuery, Pageable pageable) {
        return ResponseEntity.ok(songService.advancedSearch(songSearchQuery, pageable));
    }

    @PostMapping
    public ResponseEntity<?> saveSong(@RequestBody SongDto songDto) {
        return ResponseEntity.of(toSongDto(songService.saveSong(SaveSongCommandMapper.toSaveSongCommand(songDto))));
    }

    @DeleteMapping("/{songCode}")
    public ResponseEntity<?> deleteSong(@PathVariable String songCode) {
        songService.removeSong(songCode);
        return ResponseEntity.ok().build();
    }
}
