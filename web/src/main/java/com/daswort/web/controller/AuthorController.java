package com.daswort.web.controller;

import com.daswort.core.entity.Author;
import com.daswort.core.service.author.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAuthorList() {
        return ok(authorService.getAuthorList());
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<?> getAuthorById(@PathVariable String authorId) {
        return ok(authorService.getAuthorById(authorId));
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@RequestBody Author author) {
        return ok(authorService.addAuthor(author));
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<?> updateAuthor(@RequestBody Author author,
                                          @PathVariable String authorId) {
        return ok(authorService.updateAuthor(author, authorId));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);
        return ok().build();
    }
}
