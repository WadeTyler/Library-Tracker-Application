package net.tylerwade.librarytracker.repositories;

import net.tylerwade.librarytracker.models.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Integer> {


}
