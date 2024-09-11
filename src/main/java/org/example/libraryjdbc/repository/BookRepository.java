package org.example.libraryjdbc.repository;

import org.example.libraryjdbc.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> new Book(
            rs.getString("title"),
            rs.getString("author"),
            rs.getInt("publication_year")
    );

    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        return jdbcTemplate.query(sql, bookRowMapper, id).stream().findFirst();
    }

    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    public void update(Long id, Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
