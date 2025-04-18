package ru.otus.hw.dao;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import({TestConfig.class}) // Импортируем необходимые конфигурации
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @MockBean
    private AppProperties testFileNameProvider;

    @DisplayName("должен быть список из 4 вопросов")
    @Test
    void findAllTest() {
        when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");
        List<Question> questions = csvQuestionDao.findAll();
        assertEquals(4, questions.size());
    }
}