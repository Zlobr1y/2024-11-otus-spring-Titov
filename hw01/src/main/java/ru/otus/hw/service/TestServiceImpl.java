package ru.otus.hw.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        AtomicInteger questionCounter = new AtomicInteger(1);

        List<Question> questions = questionDao.findAll();
        questions.forEach(question -> printQuestionsAndAnswers(question, questionCounter));
    }

    private void printQuestionsAndAnswers(Question question, AtomicInteger questionCounter) {
        ioService.printLine(questionCounter.getAndIncrement() + ". Question: " + question.text());
        AtomicInteger answerCounter = new AtomicInteger(1);
        question.answers().forEach(answer ->
                ioService.printLine("\t" +answerCounter.getAndIncrement() +  ". Answer: " + answer.text()));
    }
}
