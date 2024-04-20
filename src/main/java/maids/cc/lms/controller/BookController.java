package maids.cc.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import maids.cc.lms.formdata.BookForm;
import maids.cc.lms.model.Book;
import maids.cc.lms.service.BookService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(path = "api/books", produces = "application/json")
@CrossOrigin(origins = "*")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllBooks() {
        return this.service.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") long id) {
        return this.service.getBook(id);
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> createBook(@RequestBody @Valid BookForm book) {
        return this.service.createBook(book);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<?> updateBook(@Valid Book book) {
        return this.service.updateBook(book);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        return this.service.deleteBook(id);
    }
}
