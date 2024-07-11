package com.antipin.libraryjdbc.service;

import com.antipin.libraryjdbc.model.Book;
import com.antipin.libraryjdbc.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository repository;

    public Book getById(Long id) {
        return repository.findById(id);
    }

    public List<Book> getAll() {
        return repository.findAll();
    }

    @Transactional
    public Book save(Book book) {
        return book.getId() != null ? repository.update(book) : repository.save(book);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
