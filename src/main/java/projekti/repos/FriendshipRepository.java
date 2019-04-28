package projekti.repos;

import projekti.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Friendship;

/**
 *
 * @author sonja
 */
public interface FriendshipRepository extends JpaRepository<Friendship, Long>{
    List<Friendship> findByReceiver (User receiver);
    List<Friendship> findBySender (User sender);
    Friendship findBySenderAndReceiver (User sender, User receiver);
}
