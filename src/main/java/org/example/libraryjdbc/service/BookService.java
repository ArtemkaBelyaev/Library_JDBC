package org.example.libraryjdbc.service;

import lombok.AllArgsConstructor;
import org.example.libraryjdbc.model.Book;
import org.example.libraryjdbc.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public boolean updateBook(Long id, Book book) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.update(id, book);
            return true;
        }
        return false;
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.delete(id);
            return true;
        }
        return false;
    }
}
