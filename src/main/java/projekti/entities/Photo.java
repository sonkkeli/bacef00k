package projekti.entities;

import projekti.entities.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Basic;
import org.hibernate.annotations.Type;
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
    
//    @NotEmpty
    private String description;
    
    // käytä näitä lokaalisti
//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @NotEmpty 
    
    // laita tää sit ku ollaan herokus
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;
    
    @OneToOne
    private User owner;
    
    private boolean isProfilePicture = false;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    private List<String> likers = new ArrayList<>();

    private String contentType;
    
    public void removeOldestComment(){
        Collections.sort(comments);
        comments.remove(comments.size()-1);
    }
    
}
