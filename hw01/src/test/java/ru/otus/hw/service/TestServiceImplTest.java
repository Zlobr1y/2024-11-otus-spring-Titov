package ru.otus.hw.service;

import java.util.concurrent.atomic.AtomicInteger;
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
        ioService = Mockito.mock(IOService.class);
        questionDao = Mockito.mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void testExecuteTestWithValidData() {
        List<Question> testQuestions = List.of(
                new Question("Кофе или чай?", List.of(new Answer("Кофе", true), new Answer("Чай", false))),
                new Question("2 + 2?", List.of(new Answer("4", true), new Answer("22", false)))
        );

        when(questionDao.findAll()).thenReturn(testQuestions);

        testService.executeTest();

        verify(ioService).printFormattedLine("Please answer the questions below%n");

        AtomicInteger questionCounter = new AtomicInteger(1);
        for (Question question : testQuestions) {
            verify(ioService).printLine(questionCounter.getAndIncrement() + ". Question: " + question.text());

            AtomicInteger answerCounter = new AtomicInteger(1);
            question.answers().forEach(answer ->
                    verify(ioService).printLine("\t" + answerCounter.getAndIncrement() + ". Answer: " + answer.text()));
        }
    }
}
