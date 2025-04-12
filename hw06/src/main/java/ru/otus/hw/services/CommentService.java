package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findAllByBookId(long bookId);

    Optional<Comment> findById(long id);

    void deleteById(long id);

    Comment update(long id, String text, long bookId);

    Comment insert(String text, long bookId);
}
