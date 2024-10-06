package ru.otus.hw.domain;

import com.opencsv.bean.CsvBindByName;
import java.util.List;

public record Question(@CsvBindByName(column = "questionText") String text,
                       List<Answer> answers) {

}
