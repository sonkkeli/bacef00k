package projekti.controllers;

import projekti.services.ReactionsService;
import projekti.entities.User;
import projekti.repos.UserRepository;
import projekti.services.UserService;
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
import projekti.entities.Photo;
import projekti.services.PostService;

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
    PasswordEncoder passwordEncoder;
    
    /*
    *
    *    ---- EVERYTHING RELATED TO PROFILE AND SIGN UP BELOW ----
    *
    */
    
    // ------ VIEW PROFILE ------ // 
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
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("isFriend", isFriend);
        model.addAttribute("friendrequestCanBeSent", userSer.friendrequestCanBeSent(u));
        model.addAttribute("user", u);
        model.addAttribute("posts", postSer.getPostsByProfile(u));
        model.addAttribute("photos", userSer.getPhotos(u));
        return "profile";
    }    
    
    // ------ REDIRECT FROM PROFILE TO USER'S OWN PROFILE ------ // 
    @GetMapping("/profile")
    public String viewMyPage(Model model) {
        User loggedUser = userSer.loggedInUser();
        return "redirect:/profile/" + loggedUser.getProfilename();
    }
    
    // ------ SIGN UP AND CHECKING ERRORS ------ // 
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute User user, BindingResult br, Model model) {
        
        if(br.hasErrors()) {
            System.out.println(br);
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("uniqueUsername", true);
            model.addAttribute("uniqueProfilename", true);
            return "index";
        }
        
        if ( (!userSer.checkUniqueUsername(user)) || (!userSer.checkUniqueProfilename(user)) ){
            System.out.println( "not unique" );
            model.addAttribute( "isLoggedIn", false );
            model.addAttribute( "uniqueUsername", userSer.checkUniqueUsername(user) );
            model.addAttribute( "uniqueProfilename", userSer.checkUniqueProfilename(user) );
            return "index";
        }
        
        String pw = user.getPassword();
        user.setPassword(passwordEncoder.encode(pw));
        userRepo.save(user);
        System.out.println("uusi käyttäjä lisätty");
        model.addAttribute(user);
        model.addAttribute("isLoggedIn", false);        
        model.addAttribute("uniqueUsername", true);
        model.addAttribute("uniqueProfilename", true);
        return "redirect:/login";
    }    
    
    /*
    *
    *    ---- EVERYTHING RELATED TO FRIENDS BELOW ----
    *
    */
    
    // ------ SEND FRIEND REQUEST ------ // 
    @PostMapping("/profile/{profilename}/addfriend")
    public String addFriend(@PathVariable String profilename) {
        userSer.addFriend(profilename);        
        return "redirect:/profile/" +profilename;
    }
    
    // ------ DECLINE FRIEND REQUEST ------ // 
    @PostMapping("/friends/ignore/{senderName}")
    public String ignoreFriendrequest(@PathVariable String senderName){
        userSer.ignoreFriendrequest(senderName);
        return "redirect:/friends";
    }
    
    // ------ ACCEPT FRIEND REQUEST ------ // 
    @PostMapping("/friends/accept/{senderName}")
    public String acceptFriendrequest(@PathVariable String senderName){
        userSer.acceptFriendrequest(senderName);
        return "redirect:/friends";
    }
    
    // ------ FRIENDS & REQUESTS ------ // 
    @GetMapping("/friends")
    public String showFriendsAndRequests(Model model) {
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("friends", userSer.getAllFriends());
        model.addAttribute("friendrequests", userSer.getAllFriendRequests());
        model.addAttribute("allUsers", userSer.allUsersNotYetFriends());
        return "friends";
    }
    
    // ------ FIND FRIENDS ------ // 
    @PostMapping("/finder")
    public String findFriends(@RequestParam String keyword, Model model){
        model.addAttribute("friends", userSer.getAllFriends());
        model.addAttribute("friendrequests", userSer.getAllFriendRequests());
        model.addAttribute("allUsers", userSer.notYetFriendsMatchingKeyword(keyword));
        return "friends";
    }    
    
    /*
    *
    *    ---- EVERYTHING RELATED TO PHOTOS BELOW ----
    *
    */
    
    // ------ ADD PHOTO ------ // 
    @PostMapping("/profile/{profilename}/addphoto")
    public String uploadPhoto(@RequestParam("photo") MultipartFile file, 
            @PathVariable String profilename, @RequestParam String description) {
        
        if (file != null) {
            userSer.addPhoto(file, profilename, description);
        }
        
        return "redirect:/profile/" +profilename;
    }
    
    // ------ DELETE PHOTO ------ // 
    @DeleteMapping("/profile/{profilename}/photos/{id}")
    public String delete(@PathVariable(value="id") Long id, @PathVariable String profilename){
        userSer.deletePhoto(id);
        return "redirect:/profile/" +profilename;
    }
    
    // ------ VIEW PHOTO ------ // 
    @GetMapping("/profile/{profilename}/photos/{id}")
    public ResponseEntity<byte[]> viewPhoto(@PathVariable Long id, @PathVariable String profilename) {
        Photo esim = userSer.getOnePhotoById(id);        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(esim.getContentType()));
        headers.add("Content-Disposition", "attachment; filename=" + esim.getDescription());
        return new ResponseEntity<>(esim.getContent(), headers, HttpStatus.CREATED);
    }    
    
    // ------ CHANGE PROFILE PIC ------ // 
    @PostMapping("/profile/{profilename}/photos/{id}/changeprofile")
    public String changeProfilePicture(@PathVariable String profilename, @PathVariable Long id) {
        userSer.handleProfilePicChange(profilename, id);
        return "redirect:/profile/" +profilename;
    }
}
