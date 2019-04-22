package projekti;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author sonja
 */

@Controller
public class ProfilePageController {
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UserService userSer;
    
    @Autowired
    private PostService postSer;
    
    @Autowired
    private ReactionsService reactSer;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @GetMapping("/profile/{profilename}")
    public String view(Model model, @PathVariable String profilename) {
        User u = userRepo.findByProfilename(profilename);
        // jos kyseessä oma sivu tai on jo kavereita -> ei voi lisätä uusiks
        boolean isMyProfile = false;
        if (userSer.loggedInUser().equals(u)){
            isMyProfile = true;
        }
        boolean isFriend = false; 
        if (userSer.getAllFriends().contains(u)){
            isFriend = true;
        }
        model.addAttribute("isMyProfile", isMyProfile);
        model.addAttribute("hasProfilePicture", userSer.hasProfilePicture(u));
        if (userSer.hasProfilePicture(u)){
            model.addAttribute("profilepicid", userSer.findIdOfCurrentProfilePicture(u));
        }
        model.addAttribute("isFriend", isFriend);
        model.addAttribute("user", u);
        model.addAttribute("posts", postSer.getPostsByProfile(u));
        model.addAttribute("photos", userSer.getPhotos(u));
        return "profile";
    }
    
    @PostMapping("/profile/{profilename}/addfriend")
    public String addFriend(@PathVariable String profilename) {
        userSer.addFriend(profilename);        
        return "redirect:/profile/" +profilename;
    }

    @PostMapping("/profile/{profilename}/photos/{id}/changeprofile")
    public String changeProfilePicture(@PathVariable String profilename, @PathVariable Long id) {
        userSer.handleProfilePicChange(profilename, id);
        return "redirect:/profile/" +profilename;
    }
    
    @PostMapping("/friends/ignore/{senderName}")
    public String ignoreFriendrequest(@PathVariable String senderName){
        userSer.ignoreFriendrequest(senderName);
        return "redirect:/friends";
    }
    
    @PostMapping("/friends/accept/{senderName}")
    public String acceptFriendrequest(@PathVariable String senderName){
        userSer.acceptFriendrequest(senderName);
        return "redirect:/friends";
    }
    
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute User user, BindingResult br, Model model) {
        if(br.hasErrors()) {
            System.out.println(br);
            return "index";
        }        
        String pw = user.getPassword();
        user.setPassword(passwordEncoder.encode(pw));
        userRepo.save(user);
        model.addAttribute(user);
        model.addAttribute("isLoggedIn", false);
        return "index";
    }
    
    @GetMapping("/profile")
    public String viewMyPage(Model model) {
        User loggedUser = userSer.loggedInUser();
        return "redirect:/profile/" + loggedUser.getProfilename();
    }
    
    @GetMapping("/friends")
    public String showFriendsAndRequests(Model model) {
        model.addAttribute("friends", userSer.getAllFriends());
        model.addAttribute("friendrequests", userSer.getAllFriendRequests());
        model.addAttribute("allUsers", userSer.listAllUsers());
        return "friends";
    }
    
    @PostMapping("/finder")
    public String findFriends(@RequestParam String keyword, Model model){
        model.addAttribute("friends", userSer.getAllFriends());
        model.addAttribute("friendrequests", userSer.getAllFriendRequests());
        model.addAttribute("allUsers", userSer.notYetFriendsMatchingKeyword(keyword));
        return "friends";
    }
    
    @PostMapping("/profile/{profilename}/addphoto")
    public String uploadPhoto(@RequestParam("photo") MultipartFile file, 
            @PathVariable String profilename, @RequestParam String description) {
        
        if (file != null && description != null) {
            userSer.addPhoto(file, profilename, description);
        }
        
        return "redirect:/profile/" +profilename;
    }
    
    @DeleteMapping("/profile/{profilename}/photos/{id}")
    public String delete(@PathVariable(value="id") Long id, @PathVariable String profilename){
        userSer.deletePhoto(id);
        return "redirect:/profile/" +profilename;
    }
    
    @GetMapping("/profile/{profilename}/photos/{id}")
    public ResponseEntity<byte[]> viewPhoto(@PathVariable Long id, @PathVariable String profilename) {
        Photo esim = userSer.getOnePhotoById(id);        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(esim.getContentType()));
        headers.add("Content-Disposition", "attachment; filename=" + esim.getDescription());
        return new ResponseEntity<>(esim.getContent(), headers, HttpStatus.CREATED);
    }
}
