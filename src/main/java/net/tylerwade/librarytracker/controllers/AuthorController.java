package net.tylerwade.librarytracker.controllers;

import net.tylerwade.librarytracker.models.Author;
import net.tylerwade.librarytracker.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path="/api/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    // Add a new author (CREATE)
    @PostMapping(path="/add")
    public @ResponseBody Author addNewAuthor (@RequestBody Author author) {
        Author newAuthor = new Author();
        newAuthor.setFirst_name(author.getFirst_name());
        newAuthor.setLast_name(author.getLast_name());
        authorRepository.save(newAuthor);

        return newAuthor;
    }

    // Get All Authors (READ)
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Get Author by ID (READ)
    @GetMapping(path="/author/{author_id}")
    public @ResponseBody Author getAuthorById (@PathVariable Integer author_id) {
        try {
            Optional<Author> targetOptional = authorRepository.findById(author_id);

            if (targetOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            Author target = targetOptional.get();

            return target;
        } catch (Exception e) {
            System.out.println("Error in getAuthorById Controller: " + e.getMessage());
            throw e;
        }
    }

    // Update Author Data (UPDATE)
    @PostMapping(path="/update")
    public @ResponseBody Author updateAuthor(
            @RequestParam(required = true) Integer author_id,
            @RequestBody Author newAuthor
    ) {
        try {

            // Find target author
            Optional<Author> targetOptional = authorRepository.findById(author_id);

            // If no target
            if (targetOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Not found");

            // Update Author
            Author target = targetOptional.get();
            target.setFirst_name(newAuthor.getFirst_name() != null ? newAuthor.getFirst_name() : target.getFirst_name());
            target.setLast_name(newAuthor.getLast_name() != null ? newAuthor.getLast_name() : target.getLast_name());

            authorRepository.save(target);

            return target;

        } catch (Exception e) {
            System.out.println("Error in updateAuthor Controller" + e.getMessage());
            throw e;
        }
    }

    // Delete Author (DELETE)
    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteAuthor(@RequestParam(required = true) Integer author_id) {
        try {
            if (!authorRepository.existsById(author_id)) {
                return "No author exists with that id";
            }
            authorRepository.deleteById(author_id);
            return "Author Deleted";
        } catch (Exception e) {
            System.out.println("Error in deleteAuthor Controller: " + e.getMessage());
            throw e;
        }
    }


}
