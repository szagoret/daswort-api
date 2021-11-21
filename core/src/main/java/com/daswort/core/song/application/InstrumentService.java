package com.daswort.core.song.application;

import com.daswort.core.song.domain.Instrument;
import com.daswort.core.song.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstrumentService {
    private final InstrumentRepository instrumentRepository;
    private final SongService songService;

    public void saveInstrument(Instrument instrument) {
        instrumentRepository.save(instrument);
        songService.updateRef(instrument);
    }

    public UpdateResult removeInstrument(String id) {
        if (!ObjectId.isValid(id)) {
            return UpdateResult.error();
        }
        final var hasReferences = instrumentRepository.findById(id)
                .map(songService::isReferencedBy)
                .orElse(false);
        if (hasReferences) {
            return UpdateResult.error();
        } else {
            instrumentRepository.deleteById(id);
            return UpdateResult.success();
        }
    }

}
