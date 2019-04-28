package projekti.services;

import projekti.entities.User;
import projekti.repos.UserRepository;
import projekti.services.UserService;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.entities.Post;
import projekti.repos.PostRepository;

/**
 *
 * @author sonja
 */
@Service
public class PostService {
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UserService userSer;
    
    @Autowired
    private PostRepository postRepo;
    
    // ------ ADDING POST ------ // 
    public void handlePosting(String postcontent, String profilename){
        User loggedUser = userSer.loggedInUser();
        Post p = new Post();
        p.setContent(postcontent);
        p.setWhoPosted(loggedUser);
        p.setToWhomPosted(userRepo.findByProfilename(profilename));
        p.setPostTime(new Date());
        postRepo.save(p);
        System.out.println(p);
    }
    
    // ------ POST BY PROFILE ------ // 
    public List<Post> getPostsByProfile(User u){
        List<Post> posts = postRepo.findByToWhomPosted(u);
        Collections.sort(posts);
        if (posts.size()>25){
            ArrayList<Post> karsitut = posts.stream()
                    .limit(25)
                    .collect(Collectors.toCollection(ArrayList::new));
            return karsitut;
        }
        return posts;
    }
    
    // ------ POSTS TO THE FEED ------ // 
    public List<Post> getAllPostsOfMeAndMyFriends(){
        User loggedUser = userSer.loggedInUser();
        List<Post> posts = postRepo.findByToWhomPosted(loggedUser);
        
        for (User friend : userSer.getAllFriends()){
            posts.addAll(postRepo.findByToWhomPosted(friend));
        }
        Collections.sort(posts);
        return posts;
    }
}
