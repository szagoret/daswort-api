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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.daswort.web.song.dto.SongDtoMapper.toSongDto;
import static java.util.stream.Collectors.toList;


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
    public List<SongDto> getByName(@RequestParam(defaultValue = "") String searchTerm) {
        List<Song> songsByName = songService.findByName(searchTerm);
        return songsByName.stream().map(SongDtoMapper::toSongDto).flatMap(Optional::stream).collect(toList());
    }

    @GetMapping("/search")
    public ResponseEntity<?> advancedSearch(SongSearchQuery songSearchQuery, Pageable pageable) {
        return ResponseEntity.ok(songService.advancedSearch(songSearchQuery, pageable));
    }

    @PostMapping
    public ResponseEntity<?> saveSong(@RequestBody @Valid SongDto songDto) {
        return ResponseEntity.of(toSongDto(songService.saveSong(SaveSongCommandMapper.toSaveSongCommand(songDto))));
    }

    @DeleteMapping("/{songCode}")
    public ResponseEntity<?> deleteSong(@PathVariable String songCode) {
        songService.deleteSong(songCode);
        return ResponseEntity.ok().build();
    }
}
