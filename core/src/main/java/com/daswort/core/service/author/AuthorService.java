package com.daswort.core.service.author;

import com.daswort.core.song.domain.Author;
import com.daswort.core.song.domain.Song;
import com.daswort.core.song.exception.AuthorNotFoundException;
import com.daswort.core.song.exception.AuthorReferenceException;
import com.daswort.core.repository.AuthorRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.lang.String.join;
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

        final var author = authorRepository.findById(authorId).orElseThrow(AuthorNotFoundException::new);

        Optional.ofNullable(updateAuthor.getFirstName()).ifPresent(author::setFirstName);
        Optional.ofNullable(updateAuthor.getLastName()).ifPresent(author::setLastName);

        mongoOperations.update(Author.class)
                .matching(query(where("id").is(authorId)))
                .replaceWith(author)
                .withOptions(options().upsert().returnNew())
                .as(Author.class)
                .findAndReplaceValue();

        // update author <-> song references
        List.of("melody", "arrangement", "adaptation").forEach(authorType ->
                mongoOperations.update(Song.class)
                        .matching(query(where(join(".", authorType, "_id")).is(author.getId())))
                        .apply(new Update().set(authorType, author))
                        .all());

        return author;
    }


    public Author addAuthor(Author addAuthor) {
        final var author = authorRepository.save(new Author());
        return updateAuthor(addAuthor, author.getId());
    }

    public void deleteAuthor(String authorId) {
        requireNonNull(authorId);
        final var existsAuthorMelodyReferences = mongoOperations.exists(query(where("melody._id").is(authorId)), Song.class);
        final var existsAuthorArrangementReferences = mongoOperations.exists(query(where("arrangement._id").is(authorId)), Song.class);
        final var existsAuthorAdaptationReferences = mongoOperations.exists(query(where("adaptation._id").is(authorId)), Song.class);

        if (existsAuthorMelodyReferences || existsAuthorArrangementReferences || existsAuthorAdaptationReferences) {
            throw new AuthorReferenceException(format("Unable to remove author with id: %s.", authorId));
        } else {
            authorRepository.deleteById(authorId);
        }
    }

}
