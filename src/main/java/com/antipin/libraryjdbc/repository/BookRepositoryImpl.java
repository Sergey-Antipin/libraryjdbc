package com.antipin.libraryjdbc.repository;

import com.antipin.libraryjdbc.exception.EntityNotFoundException;
import com.antipin.libraryjdbc.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository("bookRepository")
public class BookRepositoryImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> rowMapper = (rs, rowNum) ->
            new Book(rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4));

    @Override
    public Book findById(Long id) {
        String sql = "SELECT id, title, author, publication_year FROM books b WHERE b.id = ?";
        Book book;
        try {
            book = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT id, title, author, publication_year FROM books";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books(title, author, publication_year) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getPublicationYear());
            return ps;
        }, keyHolder);
        Long generatedKey = Objects.requireNonNull(keyHolder.getKey()).longValue();
        book.setId(generatedKey);
        return book;
    }

    @Override
    public Book update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());
        if (rowsAffected == 0) {
            throw new EntityNotFoundException(book.getId());
        }
        return book;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM books b WHERE b.id = ?";
        jdbcTemplate.update(sql, id);
    }
}
