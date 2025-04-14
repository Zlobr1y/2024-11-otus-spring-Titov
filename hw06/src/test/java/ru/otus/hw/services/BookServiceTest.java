package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryJpa;
import ru.otus.hw.repositories.GenreRepositoryJpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({BookServiceImpl.class, JpaAuthorRepository.class, GenreRepositoryJpa.class, BookRepositoryJpa.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookServiceTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final int EXPECTED_NUMBER_OF_BOOKS = 3;

    @Autowired
    private BookService service;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void findByIdTest() {
        var book = service.findById(FIRST_BOOK_ID);
        var expectedBook = bookRepository.findById(FIRST_BOOK_ID);
        assertThat(book).isPresent().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    public void findAllTest() {
        var expectedBooks = List.of(
                new Book(1L, "Book 1", new Author(1L, "Author 1"), new Genre(1L, "Genre 1")),
                new Book(2L, "Book 2", new Author(2L, "Author 2"), new Genre(2L, "Genre 2")),
                new Book(3L, "Book 3", new Author(3L, "Author 3"), new Genre(3L, "Genre 3"))
        );

        var actualBooks = service.findAll();

        assertThat(actualBooks)
                .isNotNull()
                .hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .usingRecursiveComparison()
                .isEqualTo(expectedBooks);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void insertTest() {
        var expectedId = 4L;
        service.insert("title", 1L, 1L);
        var expectedBook = bookRepository.findById(expectedId);
        assertTrue(expectedBook.isPresent());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteByIdTest() {
    var expectedBook = new Book(
            FIRST_BOOK_ID,
            "Expected Title",
            new Author(1L, "Expected Author"),
            new Genre(1L, "Expected Genre")
    );

    var bookBeforeDeletion = bookRepository.findById(FIRST_BOOK_ID);
    assertTrue(bookBeforeDeletion.isPresent());

    assertThat(bookBeforeDeletion.get())
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);

        service.deleteById(FIRST_BOOK_ID);
        assertTrue(bookRepository.findById(FIRST_BOOK_ID).isEmpty());
    }

    @Test
    public void updateTest() {
        var id = 3L;
        var updatedBook = service.update(id, "title", 1L, 1L);
        var expectedBook = bookRepository.findById(id);
        assertTrue(expectedBook.isPresent());

    assertThat(updatedBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook.get());
    }
}
