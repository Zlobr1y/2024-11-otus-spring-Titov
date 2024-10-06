package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {
        // Создаем контекст Spring на основе XML-файла
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        // Получаем бин TestRunnerService и запускаем тест
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}