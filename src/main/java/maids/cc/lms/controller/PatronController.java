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
import maids.cc.lms.formdata.PatronForm;
import maids.cc.lms.model.Patron;
import maids.cc.lms.service.PatronService;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(path = "api/patrons", produces = "application/json")
@CrossOrigin(origins = "*")
public class PatronController {
    private final PatronService service;

    public PatronController(PatronService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllPatrons() {
        return this.service.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatron(@PathVariable("id") long id) {
        return this.service.getPatron(id);
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> createPatron(@RequestBody @Valid PatronForm patron) {
        return this.service.createPatron(patron);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updatePatron(@PathVariable("id") Long id, @Valid @RequestBody PatronForm patronForm) {
        return this.service.updatePatron(id, patronForm);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletePatron(@PathVariable("id") long id) {
        return this.service.deletePatron(id);
    }
}
