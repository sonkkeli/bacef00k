/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author sonja
 */
@Controller
public class PostController {
    
    @Autowired
    private PostService postSer;
    
    @Autowired
    private ReactionsService reactSer;
    
    @PostMapping("/profile/{profilename}")
    public String postOnTheWall(@RequestParam String postcontent, @PathVariable String profilename){
        postSer.handlePosting(postcontent, profilename);
        return "redirect:/profile/" + profilename;
    }
    // ------ POST COMMENTING ------ // 
    @PostMapping("/profile/{profilename}/comment/{id}")
    public String commentOnPost(@RequestParam String postcommentcontent, 
            @PathVariable Long id, @PathVariable String profilename){
        
        reactSer.handlePostCommenting(postcommentcontent, profilename, id);
        return "redirect:/profile/" + profilename;
    }
    // ------ POST LIKING ------ // 
    @PostMapping("/profile/{profilename}/like/{id}")
    public String likePost(@PathVariable Long id, @PathVariable String profilename){
        
        reactSer.handlePostLike(profilename, id);
        return "redirect:/profile/" + profilename;
    }
    // ------ PHOTO COMMENTING ------ // 
    @PostMapping("/profile/{profilename}/commentPic/{id}")
    public String commentOnPhoto(@RequestParam String photocommentcontent, 
            @PathVariable Long id, @PathVariable String profilename){
        
        reactSer.handlePhotoCommenting(photocommentcontent, profilename, id);
        return "redirect:/profile/" + profilename;
    }
    // ------ PHOTO LIKING ------ // 
    @PostMapping("/profile/{profilename}/likePic/{id}")
    public String likePhoto(@PathVariable Long id, @PathVariable String profilename){
        
        reactSer.handlePhotoLike(profilename, id);
        return "redirect:/profile/" + profilename;
    }
    
    @GetMapping("/feed")
    public String showFeed(Model model) {
        model.addAttribute("posts", postSer.getAllPostsOfMeAndMyFriends());
        return "feed";
    }
}
