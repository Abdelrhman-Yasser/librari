package maids.cc.lms.formdata;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatronForm {
    @NotNull(message="Name is required")
    @NotBlank(message="Name is required")
    private final String name;
    
    @NotNull(message="Email is required")
    @NotBlank(message="Email is required")
    @Email(message = "Email is not correct")
    private final String email; 
}
