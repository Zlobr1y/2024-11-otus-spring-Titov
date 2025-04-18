package ru.otus.hw.mongo.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeUnit(id = "database-changelog", order = "001", author = "your_name")
public class DatabaseChangelog {

    private List<Genre> genres;

    private List<Author> authors;

    private List<Book> books;

    @Execution
    public void execute(MongoTemplate mongoTemplate,
                        AuthorRepository authorRepository,
                        GenreRepository genreRepository,
                        BookRepository bookRepository,
                        CommentRepository commentRepository) {

        // 1. Очистка БД
        mongoTemplate.getDb().drop();

        // 2. Добавление авторов
        authors = authorRepository.saveAll(List.of(
                new Author(new ObjectId().toString(), "Author_1"),
                new Author(new ObjectId().toString(), "Author_2"),
                new Author(new ObjectId().toString(), "Author_3")
        ));

        // 3. Добавление жанров
        genres = genreRepository.saveAll(List.of(
                new Genre(new ObjectId().toString(), "Genre_1"),
                new Genre(new ObjectId().toString(), "Genre_2"),
                new Genre(new ObjectId().toString(), "Genre_3")
        ));

        // 4. Добавление книг
        books = bookRepository.saveAll(List.of(
                new Book(new ObjectId().toString(), "BookTitle_1", authors.get(0), genres.get(0)),
                new Book(new ObjectId().toString(), "BookTitle_2", authors.get(1), genres.get(1)),
                new Book(new ObjectId().toString(), "BookTitle_3", authors.get(2), genres.get(2))
        ));

        // 5. Добавление комментариев
        commentRepository.saveAll(List.of(
                new Comment(new ObjectId().toString(), "text_1", books.get(0)),
                new Comment(new ObjectId().toString(), "text_2", books.get(0)),
                new Comment(new ObjectId().toString(), "text_3", books.get(2))
        ));
    }

    @RollbackExecution
    public void rollback() {
        // Опциональный метод для отката изменений
    }
}