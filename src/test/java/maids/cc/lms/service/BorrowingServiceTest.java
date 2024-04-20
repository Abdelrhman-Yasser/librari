package maids.cc.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import maids.cc.lms.model.Book;
import maids.cc.lms.model.BorrowingRecord;
import maids.cc.lms.model.Patron;
import maids.cc.lms.repository.BookRepository;
import maids.cc.lms.repository.BorrowingRepository;
import maids.cc.lms.repository.PatronRepository;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingService service;

    private final Book book = new Book(1, "Test Author", "Test Title", new Date());
    private final Patron patron = new Patron(1, "Test Patron", "test@example.com");
    private final BorrowingRecord record = new BorrowingRecord(book, patron);

    @Test
    public void borrowBookSuccessful() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRepository.findByBookId(anyLong())).thenReturn(Optional.empty());
        when(borrowingRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Object> response = service.borrowBook(book.getId(), patron.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof BorrowingRecord);
    }

    @Test
    public void borrowBookBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.borrowBook(book.getId(), patron.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book is not found", response.getBody());
    }

    @Test
    public void borrowBookPatronNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.borrowBook(book.getId(), book.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patron is not found", response.getBody());
    }

    @Test
    public void borrowBookBookAlreadyBorrowed() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRepository.findByBookId(anyLong())).thenReturn(Optional.of(record));

        ResponseEntity<Object> response = service.borrowBook(book.getId(), patron.getId());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book is already borrowed", response.getBody());
    }

    @Test
    public void returnBookSuccessful() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRepository.findByBookIdAndPatronId(anyLong(), anyLong())).thenReturn(Optional.of(record));

        ResponseEntity<Object> response = service.returnBook(book.getId(), patron.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void returnBookBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.returnBook(book.getId(), patron.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book is not found", response.getBody());
    }

    @Test
    public void returnBookPatronNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.returnBook(book.getId(), patron.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patron is not found", response.getBody());
    }

    @Test
    public void returnBookBookNotBorrowed() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRepository.findByBookIdAndPatronId(anyLong(), anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.returnBook(book.getId(), patron.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book is not borrowed", response.getBody());
    }
}
