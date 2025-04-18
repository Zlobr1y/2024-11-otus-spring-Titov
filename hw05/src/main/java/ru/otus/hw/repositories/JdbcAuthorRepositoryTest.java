package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.hw.models.Author;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcAuthorRepositoryTest {

    @Test
    void findById_ReturnsAuthor_WhenAuthorWithThatIdExists() throws SQLException {
        ResultSet resultSet = createResultSet(1L, "John Doe");
        NamedParameterJdbcOperations namedParameterJdbcOperations = createJdbcOperations(resultSet);

        AuthorRepository authorRepository = new JdbcAuthorRepository(namedParameterJdbcOperations);
        Optional<Author> author = authorRepository.findById(1L);

        assertThat(author.isPresent(), is(true));
        assertThat(author.get().getId(), is(1L));
        assertThat(author.get().getFullName(), is("John Doe"));

    }

    @Test
    void findById_ReturnsEmpty_WhenAuthorWithThatIdDoesnotExists() throws SQLException {
        ResultSet resultSet = createEmptyResultSet();
        NamedParameterJdbcOperations namedParameterJdbcOperations = createJdbcOperations(resultSet);

        AuthorRepository authorRepository = new JdbcAuthorRepository(namedParameterJdbcOperations);
        Optional<Author> author = authorRepository.findById(1L);

        assertThat(author.isPresent(), is(false));
    }


    private NamedParameterJdbcOperations createJdbcOperations(ResultSet resultSet) throws SQLException {
        NamedParameterJdbcOperations namedParameterJdbcOperations = mock(NamedParameterJdbcOperations.class);
        when(namedParameterJdbcOperations.query("select id, full_name from authors where id = :id",
                Collections.singletonMap("id", 1L),
                new JdbcAuthorRepository.AuthorRowMapper())).thenReturn(
                Collections.singletonList(new JdbcAuthorRepository.AuthorRowMapper().mapRow(resultSet, 1)));
        return namedParameterJdbcOperations;
    }

    private ResultSet createResultSet(long id, String fullName) throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(id);
        when(resultSet.getString("full_name")).thenReturn(fullName);
        return resultSet;
    }

    private ResultSet createEmptyResultSet() throws SQLException {
        return mock(ResultSet.class);
    }

}