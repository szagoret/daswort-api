package com.daswort.web.song;

import com.daswort.core.song.application.AuthorService;
import com.daswort.core.song.domain.Author;
import com.daswort.core.song.repository.AuthorRepository;
import com.daswort.web.song.dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000", "https://szagoret.github.io", "https://daswort.gitlab.io", "http://192.168.1.38:3000", "http://192.168.1.38:3000/daswort-ui"})
@RestController
@RequestMapping("song/authors")
@RequiredArgsConstructor
public class SongAuthorController {
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    @GetMapping("{authorId}")
    public ResponseEntity<?> findById(@PathVariable("authorId") String authorId) {
        return ResponseEntity.of(Optional.of(authorId).filter(ObjectId::isValid).flatMap(authorRepository::findById));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @DeleteMapping("{authorId}")
    public ResponseEntity<?> removeAuthor(@PathVariable("authorId") String authorId) {
        authorRepository.deleteById(authorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> saveAuthor(@RequestBody @Valid AuthorDto author) {
        authorService.saveAuthor(new Author(author.getId(), author.getFirstName(), author.getLastName()));
        return ResponseEntity.ok().build();
    }
}
