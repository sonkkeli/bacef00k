package projekti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author sonja
 */
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Post extends AbstractPersistable<Long> implements Comparable<Post> {
    
    @NotEmpty
    private String content;
    
    private Date postTime;
    
    @OneToOne
    private User whoPosted;
    
    @OneToOne
    private User toWhomPosted;
    
    @ElementCollection(targetClass = String.class)
    private List<String> likers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    
    public void removeOldestComment(){
        Collections.sort(comments);
        comments.remove(comments.size()-1);
    }
    
    @Override
    public int compareTo(Post o) {
        int returnable = 0;
        if (this.postTime.after(o.postTime)){
            returnable = -1;
        }
        if (this.postTime.before(o.postTime)){
            returnable = 1;
        }
        return returnable;
    }
}
