package net.tylerwade.librarytracker.controllers;

import net.tylerwade.librarytracker.models.Book;
import net.tylerwade.librarytracker.repositories.AuthorRepository;
import net.tylerwade.librarytracker.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path="/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    // Add a new book (CREATE)
    @PostMapping(path = "/add")
    public @ResponseBody Book addBook(@RequestBody Book book) {
        try {

            // Check for all Fields
            if (book.getBook_name() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "book_name is required");
            }
            if (book.getAuthor_id() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "author_id is required");
            }
            if (book.getIsbn() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "isbn is required");
            }
            if (book.getGenre() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "genre is required");
            }

            // Check if author exists
            if (!authorRepository.existsById(book.getAuthor_id())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no author with that id exists");
            }

            // Length checking
            if (book.getBook_name().length() < 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "book_name must be at least 2 characters long");
            }

            bookRepository.save(book);
            return book;

        } catch (Exception e) {
            System.out.println("Error in addBook Controller: " + e.getMessage());
            throw e;
        }
    }

    // Get All Books (READ)
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Book> getAllBooks() {
        Iterable<Book> books = bookRepository.findAll();
        return books;
    }

    // Get Book By ID (READ)
    @GetMapping(path = "/id/{book_id}")
    public @ResponseBody Book getBookById(@PathVariable Integer book_id) {
        Optional<Book> optionalBook = bookRepository.findById(book_id);
        if (optionalBook.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no book with that id found");

        return optionalBook.get();
    }

    // Get all books by an Author (READ)
    @GetMapping(path = "/author/{author_id}")
    public @ResponseBody Iterable<Book> getBooksByAuthor(@PathVariable Integer author_id) {

        if (!authorRepository.existsById(author_id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no author with that id exists");

        Iterable<Book> books = bookRepository.findByAuthorID(author_id);
        return books;
    }

    // Update Book (UPDATE)
    @PostMapping(path = "/update")
    public @ResponseBody Book updateBook(@RequestBody Book updatedBook) {
        if (updatedBook.getBook_id() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "book_id required");

        Optional<Book> optionalBook = bookRepository.findById(updatedBook.getBook_id());

        // Check if book is found
        if (optionalBook.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "book_id not found");

        Book target = optionalBook.get();

        // Update Values
        target.setAuthor_id(updatedBook.getAuthor_id() != null ? updatedBook.getAuthor_id() : target.getAuthor_id());
        target.setBook_name(updatedBook.getBook_name() != null ? updatedBook.getBook_name() : target.getBook_name());
        target.setIsbn(updatedBook.getIsbn() != null ? updatedBook.getIsbn() : target.getIsbn());
        target.setGenre(updatedBook.getGenre() != null ? updatedBook.getGenre() : target.getGenre());

        bookRepository.save(target);

        return target;
    }

    // Delete a Book (DELETE)
    @DeleteMapping(path = "/delete/{book_id}")
    public @ResponseBody String deleteBook(@PathVariable Integer book_id) {
        if (!bookRepository.existsById(book_id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no book with that id exists");

        bookRepository.deleteById(book_id);
        return "Book Deleted Successfully";
    }
}
