package maids.cc.lms.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import maids.cc.lms.formdata.BookForm;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final long id;
    
    @NonNull
    @NotNull
    private String author;
    
    @NonNull
    @NotNull
    private String title;
    
    @NonNull
    @NotNull
    private Date publicationYear;

    public static final Book fromBookData(BookForm data) {
        Book book = new Book();
        book.author = data.getAuthor();
        book.publicationYear = data.getPublicationYear();
        book.title = data.getTitle();
        return book;
    }
}
