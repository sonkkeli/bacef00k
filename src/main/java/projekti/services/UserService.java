package projekti.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projekti.entities.Friendship;
import projekti.repos.FriendshipRepository;
import projekti.entities.Photo;
import projekti.repos.PhotoRepository;
import projekti.entities.User;
import projekti.repos.UserRepository;

/**
 *
 * @author sonja
 */
@Service
public class UserService {
    
    @Autowired
    private PhotoRepository photoRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private FriendshipRepository friendRepo;
    
    public List<Photo> getPhotos(User u){        
        return photoRepo.findByOwner(u);        
    }
    
    public Photo getOnePhotoById(Long id){
        return photoRepo.getOne(id);
    }
    
    public void deletePhoto(Long id){
        photoRepo.delete(photoRepo.getOne(id));
    }
    
    public List<User> listAllUsers(){
        List<User> allUsers = userRepo.findAll();        
        return allUsers;
    }
    
    // ------ FILTERING FOR LISTING ALL NOT YET YOUR FRIENDS ------ // 
    public List<User> allUsersNotYetFriends(){
        List<User> usersNotMyFriends = new ArrayList<>();
        for (User u : listAllUsers()){
            if ((! getAllFriends().contains(u)) && 
                    (!u.equals(loggedInUser()))){
                
                usersNotMyFriends.add(u);
                System.out.println(u);
            }
        }        
        return usersNotMyFriends;
    }
    
    // ------ FILTERING FOR SEARCHING FRIENDS ------ // 
    public List<User> notYetFriendsMatchingKeyword(String keyword){
        List<User> usersNotMyFriends = new ArrayList<>();
        for (User u : listAllUsers()){
            if ((! getAllFriends().contains(u)) && 
                    u.getRealname().toLowerCase().contains(keyword) && 
                    (!u.equals(loggedInUser()))){
                
                usersNotMyFriends.add(u);
                System.out.println(u);
            }
        }        
        return usersNotMyFriends;
    }
    
    // ------ ADDING PHOTO ------ // 
    public void addPhoto(MultipartFile file, String profilename, String description){
        System.out.println(file.getSize());
        System.out.println(file.getContentType());
        
        if( (file.getContentType().equals("image/jpeg") || 
                file.getContentType().equals("image/jpg") || 
                file.getContentType().equals("image/png") ) && 
                file.getSize() < 10000000 && 
                getPhotos(userRepo.findByProfilename(profilename)).size() <= 10){
            
            Photo p = new Photo();
            try {
                p.setContent(file.getBytes());
                p.setOwner(userRepo.findByProfilename(profilename));
                p.setDescription(description);
                p.setContentType(file.getContentType());
                photoRepo.save(p);
            } catch (IOException ex) {
                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }
    
    // ------ FINDING OUT DETAILS OF LOGGED IN USER ------ // 
    public User loggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepo.findByUsername(auth.getName());
        return u;
    }
    
    // ------ CREATING A FRIENDSHIP (A FRIEND REQUEST, WHICH NEEDS TO BE ACCEPTED OR DECLINED) ------ // 
    public void addFriend(String profilename){
        Friendship friendship = new Friendship();
        friendship.setTimeSent(new Date());
        friendship.setSender(loggedInUser());
        friendship.setReceiver(userRepo.findByProfilename(profilename));
        friendRepo.save(friendship);
    }
    
    // ------ DECLINING FRIEND REQUEST ------ // 
    public void ignoreFriendrequest(String senderName){
        Friendship f = friendRepo.findBySenderAndReceiver(userRepo.findByProfilename(senderName), loggedInUser());
        friendRepo.delete(f);
    }
    
    // ------ ACCEPTING FRIEND REQUEST ------ // 
    public void acceptFriendrequest(String senderName){
        Friendship f = friendRepo.findBySenderAndReceiver(userRepo.findByProfilename(senderName), loggedInUser());
        f.setAccepted(true);
        friendRepo.save(f);
    }
    
    // ------ LISTING FRIENDS ------ // 
    public List<User> getAllFriends(){
        List<User> friends = new ArrayList<>();
        
        for (Friendship f : friendRepo.findBySender(loggedInUser())){
            if (f.isAccepted() && !friends.contains(f.getReceiver())){                
                friends.add(f.getReceiver());
            }
        }
        for (Friendship f : friendRepo.findByReceiver(loggedInUser())){
            if (f.isAccepted() && !friends.contains(f.getSender())){
                friends.add(f.getSender());
            }
        }        
        return friends;
    }
    
    // ------ CHANGING PROFILE PICTURE ------ // 
    public void handleProfilePicChange(String profilename, Long id){
        User u = loggedInUser();
        Photo p = photoRepo.getOne(id);
        if (hasProfilePicture(u)){
            photoRepo.getOne(findIdOfCurrentProfilePicture(u)).setProfilePicture(false);
        }
        p.setProfilePicture(true);        
        photoRepo.save(p);
    }
    
    // ------ HAS PROFILE PIC OR NOT ? ------ // 
    public boolean hasProfilePicture(User u){
        boolean hasProfilePicture = false;
        for (Photo p : photoRepo.findByOwner(u)){
            if (p.isProfilePicture()){
                hasProfilePicture = true;
            }
        }
        return hasProfilePicture;
    }
    
    // ------ ID OF CURRENT PROFILE PIC ------ // 
    public Long findIdOfCurrentProfilePicture(User u){
        Long id = -1L;
        for (Photo p : photoRepo.findByOwner(u)){
            if (p.isProfilePicture()){
                id = p.getId();
            }
        }
        System.out.println(id);
        return id;
    }
    
    // ------ LISTING FRIENDS WITH NOT YET ACCEPTED STAGE ------ // 
    public List<Friendship> getAllFriendRequests(){

        List<Friendship> friendrequests = new ArrayList<>();
        
        for (Friendship f : friendRepo.findByReceiver(loggedInUser())){
            if (! f.isAccepted()){
                friendrequests.add(f);
            }
        }
        return friendrequests;
    }
    
    // ------ UNIQUE USERNAME ------ // 
    public boolean checkUniqueUsername(User u){
        boolean unique = true;
        if ( userRepo.findByUsername(u.getUsername()) != null ){
            unique = false;
        }
        return unique;
    }
    
    // ------ UNIQUE PROFILENAME ------ // 
    public boolean checkUniqueProfilename(User u){
        boolean unique = true;
        if ( userRepo.findByProfilename(u.getProfilename()) != null ){
            unique = false;
        }
        return unique;
    }
}
