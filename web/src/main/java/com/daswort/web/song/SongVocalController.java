package com.daswort.web.song;

import com.daswort.core.song.application.VocalService;
import com.daswort.core.song.domain.Vocal;
import com.daswort.core.song.repository.VocalRepository;
import com.daswort.web.common.IdTitleDto;
import com.daswort.web.http.UpdateResultHttpResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"https://noav.vercel.app", "http://localhost:3000"})
@RestController
@RequestMapping("song/vocals")
@RequiredArgsConstructor
public class SongVocalController {
    private final VocalService vocalService;
    private final VocalRepository vocalRepository;

    @GetMapping("{vocalId}")
    public ResponseEntity<?> getById(@PathVariable("vocalId") String vocalId) {
        return ResponseEntity.of(vocalRepository.findById(vocalId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(vocalRepository.findAll());
    }

    @DeleteMapping("{vocalId}")
    public ResponseEntity<?> removeVocal(@PathVariable("vocalId") String vocalId) {
        return UpdateResultHttpResponseMapper.toResponse(vocalService.removeVocal(vocalId));
    }

    @PostMapping
    public ResponseEntity<?> saveVocal(@RequestBody @Valid IdTitleDto vocal) {
        vocalService.saveVocal(new Vocal(vocal.getId(), vocal.getTitle()));
        return ResponseEntity.ok().build();
    }
}
