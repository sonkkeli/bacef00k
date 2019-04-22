package projekti;

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
    
    public void addPhoto(MultipartFile file, String profilename, String description){
        System.out.println(file.getSize());
        System.out.println(file.getContentType());
        
        if( (file.getContentType().equals("image/jpeg") || 
                file.getContentType().equals("image/jpg") || 
                file.getContentType().equals("image/png") ) && 
                file.getSize() < 1048576 && 
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
    
    public User loggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepo.findByUsername(auth.getName());
        return u;
    }
    
    public void addFriend(String profilename){
        Friendship friendship = new Friendship();
        friendship.setTimeSent(new Date());
        friendship.setSender(loggedInUser());
        friendship.setReceiver(userRepo.findByProfilename(profilename));
        friendRepo.save(friendship);
    }
    
    public void ignoreFriendrequest(String senderName){
        Friendship f = friendRepo.findBySenderAndReceiver(userRepo.findByProfilename(senderName), loggedInUser());
        friendRepo.delete(f);
    }
    
    public void acceptFriendrequest(String senderName){
        Friendship f = friendRepo.findBySenderAndReceiver(userRepo.findByProfilename(senderName), loggedInUser());
        f.setAccepted(true);
        friendRepo.save(f);
    }
    
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
    
    public void handleProfilePicChange(String profilename, Long id){
        User u = loggedInUser();
        Photo p = photoRepo.getOne(id);
        if (hasProfilePicture(u)){
            photoRepo.getOne(findIdOfCurrentProfilePicture(u)).setProfilePicture(false);
        }
        p.setProfilePicture(true);        
        photoRepo.save(p);
    }
    
    public boolean hasProfilePicture(User u){
        boolean hasProfilePicture = false;
        for (Photo p : photoRepo.findByOwner(u)){
            if (p.isProfilePicture()){
                hasProfilePicture = true;
            }
        }
        return hasProfilePicture;
    }
    
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
    
    public List<Friendship> getAllFriendRequests(){

        List<Friendship> friendrequests = new ArrayList<>();
        
        for (Friendship f : friendRepo.findByReceiver(loggedInUser())){
            if (! f.isAccepted()){
                friendrequests.add(f);
            }
        }
        return friendrequests;
    }
}
