package ru.otus.hw.services;


import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(String id);

    List<BookDto> findAll();

    BookDto insert(BookDto bookDto);

    BookDto update(BookDto bookDto);

    void deleteById(String id);
}
