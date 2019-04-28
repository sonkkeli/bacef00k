package projekti.repos;

import projekti.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Photo;

/**
 *
 * @author sonja
 */
public interface PhotoRepository extends JpaRepository <Photo, Long>{
    List<Photo> findByOwner(User owner);
}
