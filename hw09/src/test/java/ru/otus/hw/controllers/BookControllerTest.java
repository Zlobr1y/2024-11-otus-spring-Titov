package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    private final Author testAuthor = new Author("1", "Author_1");
    private final Genre testGenre = new Genre("1", "Genre_1");
    private final BookDto testBook = new BookDto("1", "BookTitle_1", testAuthor, testGenre);

    @BeforeEach
    void setUp() {
        given(bookService.findAll()).willReturn(List.of(testBook));
        given(bookService.findById("1")).willReturn(testBook);
        given(authorService.findAll()).willReturn(List.of(testAuthor));
        given(genreService.findAll()).willReturn(List.of(testGenre));
    }

    @Test
    void getBooks_shouldReturnBooksListPage() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void getBook_shouldReturnBookDetailsPage() throws Exception {
        mvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    void createPage_shouldReturnCreateForm() throws Exception {
        mvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/create"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void createBook_shouldCreateBookAndRedirect() throws Exception {
        mvc.perform(post("/books/create")
                        .param("title", "New Book")
                        .param("author.id", "1")
                        .param("genre.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).insert(any(BookDto.class));
    }

    @Test
    void editPage_shouldReturnEditForm() throws Exception {
        mvc.perform(get("/books/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void editBook_shouldUpdateBookAndRedirect() throws Exception {
        mvc.perform(post("/books/edit")
                        .param("id", "1")
                        .param("title", "Updated Title")
                        .param("author.id", "1")
                        .param("genre.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).update(any(BookDto.class));
    }

    @Test
    void removeBook_shouldDeleteBookAndRedirect() throws Exception {
        mvc.perform(post("/books/remove").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).deleteById("1");
    }
}