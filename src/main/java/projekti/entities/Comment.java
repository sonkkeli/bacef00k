
package projekti.entities;

import projekti.entities.User;
import java.util.Date;
import javax.persistence.Entity;
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
public class Comment extends AbstractPersistable<Long> implements Comparable<Comment>{
    
    @NotEmpty
    private String content;
    
    private Date commentingTime;
    
    @OneToOne
    private User whoCommented;

    
    
    @Override
    public int compareTo(Comment o) {
        int returnable = 0;
        if (this.commentingTime.after(o.commentingTime)){
            returnable = -1;
        }
        if (this.commentingTime.before(o.commentingTime)){
            returnable = 1;
        }
        return returnable;
    }
}
