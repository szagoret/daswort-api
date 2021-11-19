package com.daswort.core.song.application;

import com.daswort.core.song.domain.Author;
import com.daswort.core.song.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final SongService songService;

    public void saveAuthor(Author author) {
        authorRepository.save(author);
        songService.updateAuthors(author);
    }

    public UpdateResult removeAuthor(String id) {
        final var hasReferences = authorRepository.findById(id)
                .map(songService::isReferencedByAuthor)
                .orElse(false);
        if (hasReferences) {
            return UpdateResult.error();
        } else {
            authorRepository.deleteById(id);
            return UpdateResult.success();
        }
    }

}
