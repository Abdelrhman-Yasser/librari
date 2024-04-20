package maids.cc.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import maids.cc.lms.formdata.BookForm;
import maids.cc.lms.model.Book;
import maids.cc.lms.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    @Test
    public void getAllBooksReturnBooksCorrectly() {
        Iterable<Book> actualList = Instancio.ofList(Book.class).size(10).create();
        
        when(repository.findAll()).thenReturn(actualList);

        ResponseEntity<?> response = this.service.getAllBooks();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof Iterable);
        assertNotNull(response.getBody());

        Iterable<Book> expectedList = (Iterable<Book>) response.getBody();

        this.compareBookLists(actualList,expectedList);
    }

    @Test
    public void getBookReturnBookCorrectly() {
        Book actualBook = Instancio.create(Book.class);
        
        when(repository.findById(anyLong())).thenReturn(Optional.of(actualBook));

        ResponseEntity<?> response = this.service.getBook(actualBook.getId());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof Book);
        assertNotNull(response.getBody());

        Book expectedBook = (Book) response.getBody();

        assertEquals(expectedBook.getId(), actualBook.getId());
    }

    @Test
    public void canCreateBook() {
        BookForm bookForm = Instancio.create(BookForm.class);
        Book actualBook = Book.fromBookData(bookForm);
        
        when(repository.save(any())).thenAnswer(invocation -> {
            Field idField = Book.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(actualBook, 1L);
            return actualBook;
        });

        ResponseEntity<?> response = this.service.createBook(bookForm);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(response.getBody() instanceof Book);
        assertNotNull(response.getBody());

        Book expectedBook = (Book) response.getBody();
        assertEquals(expectedBook.getId(), 1);
        assertEquals(expectedBook.getAuthor(), bookForm.getAuthor());
        assertEquals(expectedBook.getTitle(), bookForm.getTitle());
        assertEquals(expectedBook.getPublicationYear(), bookForm.getPublicationYear());
    }

    @Test
    public void canUpdateBook() {
        BookForm bookForm = Instancio.create(BookForm.class);
        Book actualBook = Book.fromBookData(bookForm);
        actualBook.setId(1L);
                
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any())).thenReturn(actualBook);

        ResponseEntity<?> response = this.service.updateBook(actualBook.getId(), bookForm);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(response.getBody() instanceof Book);
        assertNotNull(response.getBody());

        Book expectedBook = (Book) response.getBody();
        assertEquals(expectedBook.getId(), actualBook.getId());
        assertEquals(expectedBook.getAuthor(), actualBook.getAuthor());
        assertEquals(expectedBook.getTitle(), actualBook.getTitle());
        assertEquals(expectedBook.getPublicationYear(), actualBook.getPublicationYear());
    }

    @Test
    public void updateBookHandleNotFound() {
        BookForm bookForm = Instancio.create(BookForm.class);
        Book actualBook = Book.fromBookData(bookForm);
        actualBook.setId(1L);
                
        when(repository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<?> response = this.service.updateBook(actualBook.getId(), bookForm);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(response.getBody() instanceof String);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), BookService.NOT_FOUND_ERROR);
    }

    @Test
    public void canDeleteBook() {
        Book actualBook = Instancio.create(Book.class);
        
        when(repository.existsById(anyLong())).thenReturn(true);
        
        ResponseEntity<?> response = this.service.deleteBook(actualBook.getId());
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertNull(response.getBody());
    }

    @Test
    public void deleteBookHandleNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<?> response = this.service.deleteBook(1L);
        
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(response.getBody() instanceof String);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), BookService.NOT_FOUND_ERROR);
    }

    private void compareBookLists(Iterable<Book> expectedList, Iterable<Book> actualList) {
        Iterator<Book> expectedIterator = expectedList.iterator();
        Iterator<Book> actualIterator = actualList.iterator();
       
        assertTrue(expectedIterator.hasNext());
        
        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            Book expectedBook = expectedIterator.next();
            Book actualBook = actualIterator.next();
            assertEquals(expectedBook.getId(), actualBook.getId());
        }

        assertFalse(expectedIterator.hasNext(), "Expected iterator has more elements");
        assertFalse(actualIterator.hasNext(), "Actual iterator has more elements");
    }
}
