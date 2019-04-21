package projekti;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author sonja
 */
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Friendship extends AbstractPersistable<Long> {
    private Date timeSent;
    @OneToOne
    private User sender;
    @OneToOne
    private User receiver;
    private boolean accepted = false;
}
