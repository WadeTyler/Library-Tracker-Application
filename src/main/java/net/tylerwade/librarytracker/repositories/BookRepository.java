package net.tylerwade.librarytracker.repositories;

import net.tylerwade.librarytracker.models.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {
    @Query(value = "SELECT * FROM books WHERE author_id = ?", nativeQuery = true)
    public Iterable<Book> findByAuthorID(Integer author_id);
}
