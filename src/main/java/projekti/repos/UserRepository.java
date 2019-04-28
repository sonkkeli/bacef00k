package projekti.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.User;

/**
 *
 * @author sonja
 */
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername (String username);
    User findByProfilename (String profilename);
}
