package com.daswort.web.song;

import com.daswort.core.song.domain.Vocal;
import com.daswort.core.song.repository.VocalRepository;
import com.daswort.web.common.IdTitleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io", "http://192.168.1.38:3000", "http://192.168.1.38:3000/daswort-ui"})
@RestController
@RequestMapping("song/vocals")
@RequiredArgsConstructor
public class SongVocalController {
    private final VocalRepository vocalRepository;

    @GetMapping("{vocalId}")
    public ResponseEntity<?> findById(@PathVariable("vocalId") String vocalId) {
        return ResponseEntity.of(vocalRepository.findById(vocalId));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(vocalRepository.findAll());
    }

    @DeleteMapping("{vocalId}")
    public ResponseEntity<?> removeVocal(@PathVariable("vocalId") String vocalId) {
        vocalRepository.deleteById(vocalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> saveVocal(@RequestBody @Valid IdTitleDto vocal) {
        vocalRepository.save(new Vocal(vocal.getId(), vocal.getTitle()));
        return ResponseEntity.ok().build();
    }
}
