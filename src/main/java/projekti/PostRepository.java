/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sonja
 */
public interface PostRepository extends JpaRepository <Post, Long>{
    List<Post> findByWhoPosted(User whoPosted);
    List<Post> findByToWhomPosted(User toWhomPosted);
}
