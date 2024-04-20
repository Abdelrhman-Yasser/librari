package maids.cc.lms.formdata;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookForm {
    @NotNull(message="Author is required")
    @NotBlank(message="Author is required")
    private String author;
    
    @NotNull(message="Title is required")
    @NotBlank(message="Title is required")
    private String title;
    
    @NotNull(message="Publication year is required")
    private Date publicationYear;
}
