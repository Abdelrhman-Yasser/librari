package maids.cc.lms.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import maids.cc.lms.formdata.PatronForm;
import maids.cc.lms.model.Patron;
import maids.cc.lms.repository.PatronRepository;

@Service
public class PatronService {

    public static final String NOT_FOUND_ERROR = "Patron is not found";

    private final PatronRepository repository;

    public PatronService(PatronRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<Object> getAllPatrons() {
        Iterable<Patron> patrons = this.repository.findAll();

        return new ResponseEntity<>(patrons, HttpStatus.OK);
    }

    public ResponseEntity<Object> getPatron(long id) {
        Optional<Patron> patron = this.repository.findById(id);

        if (patron.isPresent()) {
            return new ResponseEntity<>(patron.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> createPatron(PatronForm patronForm) {
        Patron created = this.repository.save(Patron.fromPatronData(patronForm));
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updatePatron(long id, PatronForm patronForm) {
        if (!this.repository.existsById(id)) {
            return new ResponseEntity<>(NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
        }

        Patron patron = Patron.fromPatronData(patronForm);
        patron.setId(id);

        Patron updated = this.repository.save(patron);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deletePatron(long id) {
        if (!this.repository.existsById(id)) {
            return new ResponseEntity<>(NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
        }

        this.repository.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
