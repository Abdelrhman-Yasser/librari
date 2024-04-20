package maids.cc.lms.model;

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
import maids.cc.lms.formdata.PatronForm;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Data
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final long id;
    
    @NonNull
    @NotNull
    private String name;
    
    @NonNull
    @NotNull
    private String email; 

    public static final Patron fromPatronData(PatronForm data) {
        Patron patron = new Patron();
        patron.name = data.getName();
        patron.email = data.getEmail();
        return patron;
    }
}
