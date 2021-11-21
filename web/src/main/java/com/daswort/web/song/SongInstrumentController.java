package com.daswort.web.song;

import com.daswort.core.song.application.InstrumentService;
import com.daswort.core.song.domain.Instrument;
import com.daswort.core.song.repository.InstrumentRepository;
import com.daswort.web.common.IdTitleDto;
import com.daswort.web.http.UpdateResultHttpResponseMapper;
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
    private final InstrumentService instrumentService;

    @GetMapping("{instrumentId}")
    public ResponseEntity<?> getById(@PathVariable("instrumentId") String instrumentId) {
        return ResponseEntity.of(instrumentRepository.findById(instrumentId));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(instrumentRepository.findAll());
    }

    @DeleteMapping("{instrumentId}")
    public ResponseEntity<?> removeInstrument(@PathVariable("instrumentId") String instrumentId) {
        return UpdateResultHttpResponseMapper.toResponse(instrumentService.removeInstrument(instrumentId));
    }

    @PostMapping
    public ResponseEntity<?> saveInstrument(@RequestBody @Valid IdTitleDto instrument) {
        instrumentService.saveInstrument(new Instrument(instrument.getId(), instrument.getTitle()));
        return ResponseEntity.ok().build();
    }
}
