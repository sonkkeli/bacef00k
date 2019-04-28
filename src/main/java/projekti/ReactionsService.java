
package projekti;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author sonja
 */

@Service
public class ReactionsService {
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UserService userSer;
    
    @Autowired
    private PhotoRepository photoRepo;
    
    @Autowired
    private PostRepository postRepo;
    
    @Autowired
    private CommentRepository commentRepo;
    
    // ------ POST COMMENTING ------ // 
    public void handlePostCommenting(String postcommentcontent, String profilename, Long id){
        Comment c = new Comment();
        c.setCommentingTime(new Date());
        c.setContent(postcommentcontent);
        c.setWhoCommented(userSer.loggedInUser());
        commentRepo.save(c);
        Post p = postRepo.getOne(id);
        if (p.getComments().size() >= 10){
            p.removeOldestComment();
        }
        p.getComments().add(c);
        postRepo.save(p);
    }
    
    // ------ POST LIKING ------ // 
    public void handlePostLike(String profilename, Long id){        
        Post p = postRepo.getOne(id);
        if (! alreadyLiked(p)){            
            p.getLikers().add(profilename);
            postRepo.save(p);
        }        
    }    
    
    // ------ PHOTO COMMENTING ------ // 
    public void handlePhotoCommenting(String photocommentcontent, String profilename, Long id){
        Comment c = new Comment();
        c.setCommentingTime(new Date());
        c.setContent(photocommentcontent);
        c.setWhoCommented(userSer.loggedInUser());
        commentRepo.save(c);
        Photo p = photoRepo.getOne(id);
        p.getComments().add(c);
        photoRepo.save(p);
    }
    
    // ------ PHOTO LIKING ------ // 
    public void handlePhotoLike(String profilename, Long id){        
        Photo p = photoRepo.getOne(id);
        if (! alreadyLiked(p)){            
            p.getLikers().add(profilename);
            photoRepo.save(p);
        }        
    }
    
    // ------ POST ------ // 
    public boolean alreadyLiked(Post p){
        boolean alreadyLiked = false;
        for(String liker : p.getLikers()){
            if (liker.equals(userSer.loggedInUser().getProfilename())){
                alreadyLiked = true;
                break;
            }
        }
        return alreadyLiked;
    }
    
    // ------ PHOTO ------ // 
    public boolean alreadyLiked(Photo p){
        boolean alreadyLiked = false;
        for(String liker : p.getLikers()){
            if (liker.equals(userSer.loggedInUser().getProfilename())){
                alreadyLiked = true;
                break;
            }
        }
        return alreadyLiked;
    }
}
