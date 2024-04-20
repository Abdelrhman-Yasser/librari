package maids.cc.lms.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import maids.cc.lms.formdata.BookForm;
import maids.cc.lms.model.Book;
import maids.cc.lms.repository.BookRepository;

@Service
public class BookService {

    public static final String NOT_FOUND_ERROR = "Book is not found";

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> getAllBooks() {
        Iterable<Book> books = this.repository.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<?> getBook(long id) {
        Optional<Book> book = this.repository.findById(id);

        if (book.isPresent()) {
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> createBook(BookForm bookForm) {
        Book created = this.repository.save(Book.fromBookData(bookForm));
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateBook(Book book) {
        if (!this.repository.existsById(book.getId())) {
            return new ResponseEntity<>(NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
        }

        Book updated = this.repository.save(book);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteBook(long id) {
        if (!this.repository.existsById(id)) {
            return new ResponseEntity<>(NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
        }

        this.repository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
