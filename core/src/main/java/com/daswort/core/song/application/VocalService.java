package com.daswort.core.song.application;

import com.daswort.core.song.domain.UpdateErrorMessage;
import com.daswort.core.song.domain.UpdateResult;
import com.daswort.core.song.domain.Vocal;
import com.daswort.core.song.repository.VocalRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VocalService {
    private final VocalRepository vocalRepository;
    private final SongService songService;

    public void saveVocal(Vocal vocal) {
        vocalRepository.save(vocal);
        songService.updateRef(vocal);
    }

    public UpdateResult removeVocal(String id) {
        if (!ObjectId.isValid(id)) {
            return UpdateResult.error(UpdateErrorMessage.INVALID_OBJECT_ID);
        }
        final var hasReferences = vocalRepository.findById(id)
                .map(songService::isReferencedBy)
                .orElse(false);
        if (hasReferences) {
            return UpdateResult.error(UpdateErrorMessage.REFERENCE_CONSTRAINT_ERROR);
        } else {
            vocalRepository.deleteById(id);
            return UpdateResult.success();
        }
    }

}
