package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sonja
 */
public interface PhotoRepository extends JpaRepository <Photo, Long>{
    List<Photo> findByOwner(User owner);
}
