package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.dao.QuestionDao;

import java.util.List;

import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private IOService ioService;
    private QuestionDao questionDao;
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = Mockito.mock(IOService.class);  // Мок IOService
        questionDao = Mockito.mock(QuestionDao.class);  // Мок QuestionDao
        testService = new TestServiceImpl(ioService, questionDao);  // Создаем экземпляр тестируемого класса
    }

    @Test
    void executeTest_ShouldPrintQuestionsAndAnswers() {
        // Подготовка данных
        Answer answer1 = new Answer("Answer 1", true);
        Answer answer2 = new Answer("Answer 2", false);
        Question question1 = new Question("Question 1", List.of(answer1, answer2));

        when(questionDao.findAll()).thenReturn(List.of(question1));  // Мокаем возвращаемое значение из findAll()

        // Выполняем тестируемый метод
        testService.executeTest();

        // Проверяем взаимодействие с ioService
        verify(ioService).printLine("");  // Проверка вызова пустой строки
        verify(ioService).printFormattedLine("Please answer the questions below%n");  // Проверка форматированной строки
        verify(ioService).printLine("Question: Question 1");  // Проверка вопроса
        verify(ioService).printLine("Answer: Answer 1");  // Проверка ответа 1
        verify(ioService).printLine("Answer: Answer 2");  // Проверка ответа 2

        // Убеждаемся, что взаимодействие с questionDao произошло один раз
        verify(questionDao, times(1)).findAll();
    }
}
