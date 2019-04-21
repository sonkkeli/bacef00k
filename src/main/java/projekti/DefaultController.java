package projekti;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DefaultController {
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private UserService userSer;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() {
        User testUser = new User();
        String username = "username";        
        testUser.setUsername("username");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRealname("realname");
        testUser.setProfilename("profilename");    
        User foundAccount = userRepo.findByUsername(username);
        if (foundAccount == null) {
            userRepo.save(testUser);
        }
    }  

    @GetMapping("*")
    public String landingPage(Model model) {
        boolean isLoggedIn = false;
        if (userSer.loggedInUser() != null){
            isLoggedIn = true;
        }
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "index";
    }
}
