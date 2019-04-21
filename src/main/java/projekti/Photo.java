package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author sonja
 */

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Photo extends AbstractPersistable<Long>{
    
    @NotEmpty
    private String description;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty
    private byte[] content;
    
    @OneToOne
    private User owner;
    
    private boolean isProfilePicture = false;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    private List<String> likers = new ArrayList<>();

    private String contentType;
}
