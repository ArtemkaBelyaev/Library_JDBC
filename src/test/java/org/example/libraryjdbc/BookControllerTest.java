package org.example.libraryjdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.libraryjdbc.controller.BookController;
import org.example.libraryjdbc.model.Book;
import org.example.libraryjdbc.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private ObjectMapper objectMapper;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        book1 = new Book("Example book", "Mikhail Larin", 2024);
        book1.setId(1L);
        book2 = new Book("Example Book", "Artem Belyaev", 2023);
        book2.setId(2L);
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Mockito.when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(book1.getId()))
                .andExpect(jsonPath("$[0].title").value(book1.getTitle()))
                .andExpect(jsonPath("$[0].author").value(book1.getAuthor()))
                .andExpect(jsonPath("$[0].publicationYear").value(book1.getPublicationYear()))
                .andExpect(jsonPath("$[1].id").value(book2.getId()))
                .andExpect(jsonPath("$[1].title").value(book2.getTitle()))
                .andExpect(jsonPath("$[1].author").value(book2.getAuthor()))
                .andExpect(jsonPath("$[1].publicationYear").value(book2.getPublicationYear()));
    }

    @Test
    public void testGetBookByIdSuccess() throws Exception {
        Mockito.when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(book1.getId()))
                .andExpect(jsonPath("$.title").value(book1.getTitle()))
                .andExpect(jsonPath("$.author").value(book1.getAuthor()))
                .andExpect(jsonPath("$.publicationYear").value(book1.getPublicationYear()));
    }

    @Test
    public void testGetBookByIdUnsuccessful() throws Exception {
        Mockito.when(bookService.getBookById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddBook() throws Exception {
        Book newBook = new Book("Marimba", "Miyagi", 2021);
        String bookJson = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk());

        Mockito.verify(bookService).addBook(any(Book.class));
    }

    @Test
    public void testUpdateBookSuccess() throws Exception {
        Book updatedBook = new Book("Animal Farm", "George Orwell", 1945);
        updatedBook.setId(1L);
        String bookJson = objectMapper.writeValueAsString(updatedBook);

        Mockito.when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(true);
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk());
        Mockito.verify(bookService).updateBook(eq(1L), any(Book.class));
    }

    @Test
    public void testUpdateBookUnsuccessful() throws Exception {
        Book updatedBook = new Book("Animal Farm", "George Orwell", 1945);
        updatedBook.setId(1L);
        String bookJson = objectMapper.writeValueAsString(updatedBook);

        Mockito.when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(false);
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isNotFound());
        Mockito.verify(bookService).updateBook(eq(1L), any(Book.class));
    }

    @Test
    public void testDeleteBookSuccess() throws Exception {
        Mockito.when(bookService.deleteBook(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
        Mockito.verify(bookService).deleteBook(1L);
    }

    @Test
    public void testDeleteBookUnsuccessful() throws Exception {
        Mockito.when(bookService.deleteBook(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound());
        Mockito.verify(bookService).deleteBook(1L);
    }
}