package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;


    @Override
    public List<Question> findAll() {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        InputStream inputStream = getClass().getResourceAsStream("/questions.csv");
        if (inputStream == null) {
            try {
                throw new FileNotFoundException("Resource not found: /questions.csv");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        try (Reader reader = new InputStreamReader(inputStream)) {
            CsvToBean<QuestionDto> csvToBean = getCsvToBean(reader);
            List<QuestionDto> dtos = csvToBean.parse();
            return dtos.stream()
                    .map(dto -> new Question(dto.getText(), dto.getAnswers()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuestionReadException("Error reading questions from CSV", e);
        }
    }



    private static CsvToBean<QuestionDto> getCsvToBean(Reader reader) {
        return new CsvToBeanBuilder<QuestionDto>(reader)
                .withType(QuestionDto.class)
                .withSkipLines(1)
                .withSeparator(';')
                .build();
    }
}
