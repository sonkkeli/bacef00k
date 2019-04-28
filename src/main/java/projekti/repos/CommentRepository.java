
package projekti.repos;

import projekti.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sonja
 */
public interface CommentRepository extends JpaRepository <Comment, Long>{
}
