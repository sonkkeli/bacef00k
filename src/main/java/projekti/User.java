package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author sonja
 */

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class User extends AbstractPersistable<Long>{
    
    @NotEmpty
    @Size(min = 4, max = 15)
    private String username;
    
    @NotEmpty
    @Size(min = 4, max = 150)
    private String password;
    
    @NotEmpty
    @Size(min = 4, max = 30)
    private String realname;
    
    @NotEmpty
    @Size(min = 4, max = 15)
    private String profilename;
    
//    private Photo profilePicture;

}
