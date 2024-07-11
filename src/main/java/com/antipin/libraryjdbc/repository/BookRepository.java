package com.antipin.libraryjdbc.repository;

import com.antipin.libraryjdbc.model.Book;

import java.util.List;

public interface BookRepository {

    Book findById(Long id);

    List<Book> findAll();

    Book save(Book book);

    Book update(Book book);

    void deleteById(Long id);
}
