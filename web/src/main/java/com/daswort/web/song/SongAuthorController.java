package com.daswort.web.song;

import com.daswort.core.song.application.AuthorService;
import com.daswort.core.song.domain.Author;
import com.daswort.core.song.repository.AuthorRepository;
import com.daswort.web.http.UpdateResultHttpResponseMapper;
import com.daswort.web.song.dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("song/authors")
@RequiredArgsConstructor
public class SongAuthorController {
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    @GetMapping("{authorId}")
    public ResponseEntity<?> getById(@PathVariable("authorId") String authorId) {
        return ResponseEntity.of(Optional.of(authorId).filter(ObjectId::isValid).flatMap(authorRepository::findById));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @DeleteMapping("{authorId}")
    public ResponseEntity<?> removeAuthor(@PathVariable("authorId") String authorId) {
        return UpdateResultHttpResponseMapper.toResponse(authorService.removeAuthor(authorId));
    }

    @PostMapping
    public ResponseEntity<?> saveAuthor(@RequestBody @Valid AuthorDto author) {
        authorService.saveAuthor(new Author(author.getId(), author.getName()));
        return ResponseEntity.ok().build();
    }
}
