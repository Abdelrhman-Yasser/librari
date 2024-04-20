package maids.cc.lms.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import maids.cc.lms.model.Book;
import maids.cc.lms.model.BorrowingRecord;
import maids.cc.lms.model.Patron;
import maids.cc.lms.repository.BookRepository;
import maids.cc.lms.repository.BorrowingRepository;
import maids.cc.lms.repository.PatronRepository;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public BorrowingService(BorrowingRepository borrowingRepository, BookRepository bookRepository,
            PatronRepository patronRepository) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    public ResponseEntity<Object> borrowBook(long bookId, long patronId) {
        try {
            Optional<Book> book = this.bookRepository.findById(bookId);
            if (!book.isPresent()) {
                return new ResponseEntity<>("Book is not found", HttpStatus.NOT_FOUND);
            }

            Optional<Patron> patron = this.patronRepository.findById(patronId);
            if (!patron.isPresent()) {
                return new ResponseEntity<>("Patron is not found", HttpStatus.NOT_FOUND);
            }

            if(this.borrowingRepository.findByBookId(bookId).isPresent()) {
                return new ResponseEntity<>("Book is already borrowed", HttpStatus.BAD_REQUEST);
            }

            BorrowingRecord record = new BorrowingRecord(book.get(), patron.get());
            record = this.borrowingRepository.save(record);
            return new ResponseEntity<>(record, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> returnBook(long bookId, long patronId) {
        try {
            Optional<Book> book = this.bookRepository.findById(bookId);
            if (!book.isPresent()) {
                return new ResponseEntity<>("Book is not found", HttpStatus.NOT_FOUND);
            }

            Optional<Patron> patron = this.patronRepository.findById(patronId);
            if (!patron.isPresent()) {
                return new ResponseEntity<>("Patron is not found", HttpStatus.NOT_FOUND);
            }

            Optional<BorrowingRecord> record = this.borrowingRepository.findByBookIdAndPatronId(book.get().getId(),
                    patron.get().getId());

            if(!record.isPresent()) {
                return new ResponseEntity<>("Book is not borrowed", HttpStatus.NOT_FOUND);
            }

            this.borrowingRepository.delete(record.get());
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
