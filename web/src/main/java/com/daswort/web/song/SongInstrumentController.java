package com.daswort.web.song;

import com.daswort.core.song.domain.Instrument;
import com.daswort.core.song.repository.InstrumentRepository;
import com.daswort.web.common.IdTitleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io", "http://192.168.1.38:3000", "http://192.168.1.38:3000/daswort-ui"})
@RestController
@RequestMapping("song/instruments")
@RequiredArgsConstructor
public class SongInstrumentController {
    private final InstrumentRepository instrumentRepository;

    @GetMapping("{instrumentId}")
    public ResponseEntity<?> findById(@PathVariable("instrumentId") String instrumentId) {
        return ResponseEntity.of(instrumentRepository.findById(instrumentId));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(instrumentRepository.findAll());
    }

    @DeleteMapping("{instrumentId}")
    public ResponseEntity<?> removeInstrument(@PathVariable("instrumentId") String instrumentId) {
        instrumentRepository.deleteById(instrumentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> saveInstrument(@RequestBody @Valid IdTitleDto instrument) {
        instrumentRepository.save(new Instrument(instrument.getId(), instrument.getTitle()));
        return ResponseEntity.ok().build();
    }
}
