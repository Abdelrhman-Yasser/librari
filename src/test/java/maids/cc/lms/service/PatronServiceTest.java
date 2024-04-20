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

import maids.cc.lms.formdata.PatronForm;
import maids.cc.lms.model.Patron;
import maids.cc.lms.repository.PatronRepository;

@ExtendWith(MockitoExtension.class)
public class PatronServiceTest {

    @Mock
    private PatronRepository repository;

    @InjectMocks
    private PatronService service;

    @Test
    public void getAllPatronsReturnPatronsCorrectly() {
        Iterable<Patron> actualList = Instancio.ofList(Patron.class).size(10).create();
        
        when(repository.findAll()).thenReturn(actualList);

        ResponseEntity<?> response = this.service.getAllPatrons();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof Iterable);
        assertNotNull(response.getBody());

        Iterable<Patron> expectedList = (Iterable<Patron>) response.getBody();

        // Compare the expected and actual lists
        comparePatronLists(expectedList, actualList);
    }

    @Test
    public void getPatronReturnPatronCorrectly() {
        Patron actualPatron = Instancio.create(Patron.class);
        
        when(repository.findById(anyLong())).thenReturn(Optional.of(actualPatron));

        ResponseEntity<?> response = this.service.getPatron(actualPatron.getId());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody() instanceof Patron);
        assertNotNull(response.getBody());

        Patron expectedPatron = (Patron) response.getBody();

        assertEquals(expectedPatron.getId(), actualPatron.getId());
    }

    @Test
    public void canCreatePatron() {
        PatronForm patronForm = Instancio.create(PatronForm.class);
        Patron actualPatron = Patron.fromPatronData(patronForm);
        
        when(repository.save(any())).thenAnswer(invocation -> {
            Field idField = Patron.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(actualPatron, 1L);
            return actualPatron;
        });

        ResponseEntity<?> response = this.service.createPatron(patronForm);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(response.getBody() instanceof Patron);
        assertNotNull(response.getBody());

        Patron expectedPatron = (Patron) response.getBody();
        assertEquals(expectedPatron.getId(), 1L);
        assertEquals(expectedPatron.getName(), patronForm.getName());
        assertEquals(expectedPatron.getEmail(), patronForm.getEmail());
    }

    @Test
    public void canUpdatePatron() {
        Patron actualPatron = Instancio.create(Patron.class);
                
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any())).thenReturn(actualPatron);

        ResponseEntity<?> response = this.service.updatePatron(actualPatron);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertTrue(response.getBody() instanceof Patron);
        assertNotNull(response.getBody());

        Patron expectedPatron = (Patron) response.getBody();
        assertEquals(expectedPatron.getId(), actualPatron.getId());
        assertEquals(expectedPatron.getName(), actualPatron.getName());
        assertEquals(expectedPatron.getEmail(), actualPatron.getEmail());
    }

    @Test
    public void updatePatronHandleNotFound() {
        Patron actualPatron = Instancio.create(Patron.class);
                
        when(repository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<?> response = this.service.updatePatron(actualPatron);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(response.getBody() instanceof String);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), PatronService.NOT_FOUND_ERROR);
    }

    @Test
    public void canDeletePatron() {
        Patron actualPatron = Instancio.create(Patron.class);
        
        when(repository.existsById(anyLong())).thenReturn(true);
        
        ResponseEntity<?> response = this.service.deletePatron(actualPatron.getId());
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertNull(response.getBody());
    }

    @Test
    public void deletePatronHandleNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<?> response = this.service.deletePatron(1L);
        
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertTrue(response.getBody() instanceof String);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), PatronService.NOT_FOUND_ERROR);
    }

    private void comparePatronLists(Iterable<Patron> expectedList, Iterable<Patron> actualList) {
        Iterator<Patron> expectedIterator = expectedList.iterator();
        Iterator<Patron> actualIterator = actualList.iterator();
       
        assertTrue(expectedIterator.hasNext());
        
        while (expectedIterator.hasNext() && actualIterator.hasNext()) {
            Patron expectedPatron = expectedIterator.next();
            Patron actualPatron = actualIterator.next();
            assertEquals(expectedPatron.getId(), actualPatron.getId());
        }

        assertFalse(expectedIterator.hasNext(), "Expected iterator has more elements");
        assertFalse(actualIterator.hasNext(), "Actual iterator has more elements");
    }
}
