package com.daswort.core.song.application;

import com.daswort.core.song.domain.Author;
import com.daswort.core.song.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final SongService songService;

    public void saveAuthor(Author author) {
        authorRepository.save(author);
        songService.updateRef(author);
    }

    public UpdateResult removeAuthor(String id) {
        if (!ObjectId.isValid(id)) {
            return UpdateResult.error(UpdateErrorMessage.INVALID_OBJECT_ID);
        }
        final var hasReferences = authorRepository.findById(id)
                .map(songService::isReferencedBy)
                .orElse(false);
        if (hasReferences) {
            return UpdateResult.error(UpdateErrorMessage.REFERENCE_CONSTRAINT_ERROR);
        } else {
            authorRepository.deleteById(id);
            return UpdateResult.success();
        }
    }

}
