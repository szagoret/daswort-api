package com.daswort.core.service.author;

import com.daswort.core.entity.Author;
import com.daswort.core.entity.Song;
import com.daswort.core.exception.AuthorNotFoundException;
import com.daswort.core.exception.AuthorReferenceException;
import com.daswort.core.repository.AuthorRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.data.mongodb.core.FindAndReplaceOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final MongoOperations mongoOperations;

    public AuthorService(AuthorRepository authorRepository,
                         MongoOperations mongoOperations) {
        this.authorRepository = authorRepository;
        this.mongoOperations = mongoOperations;
    }

    public List<Author> getAuthorList() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(String authorId) {
        requireNonNull(authorId);
        return authorRepository.findById(authorId)
                .orElseThrow(AuthorNotFoundException::new);
    }

    public Author updateAuthor(Author updateAuthor, String authorId) {
        requireNonNull(updateAuthor);
        requireNonNull(authorId);

        // todo update all songs that contain this author

        final var author = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);

        Optional.ofNullable(updateAuthor.getFirstName()).ifPresent(author::setFirstName);
        Optional.ofNullable(updateAuthor.getLastName()).ifPresent(author::setLastName);

        return mongoOperations.update(Author.class)
                .matching(query(where("id").is(authorId)))
                .replaceWith(author)
                .withOptions(options().upsert().returnNew())
                .as(Author.class)
                .findAndReplaceValue();
    }


    public Author addAuthor(Author addAuthor) {
        final var author = authorRepository.save(new Author());
        return updateAuthor(addAuthor, author.getId());
    }

    public void deleteAuthor(String authorId) {
        requireNonNull(authorId);
        final var existsAuthorReferences = mongoOperations.exists(query(where("author._id").is(authorId)), Song.class);
        if (existsAuthorReferences) {
            throw new AuthorReferenceException();
        } else {
            authorRepository.deleteById(authorId);
        }
    }

}
