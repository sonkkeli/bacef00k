package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sonja
 */
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername (String username);
    User findByProfilename (String profilename);
}
