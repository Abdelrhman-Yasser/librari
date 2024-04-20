package maids.cc.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import maids.cc.lms.service.BorrowingService;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping(path = "/api", produces = "application/json")
@CrossOrigin(origins = "*")
public class BorrowController {

    private final BorrowingService service;
    
    public BorrowController(BorrowingService service) {
        this.service = service;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<?> borrowBook(@PathVariable("patronId") Long patronId, @PathVariable("bookId") Long bookId) {
        return this.service.borrowBook(bookId, patronId);
    }

    @DeleteMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<?> returnBook(@PathVariable("patronId") Long patronId, @PathVariable("bookId") Long bookId) {
        return this.service.returnBook(bookId, patronId);
    }
}
