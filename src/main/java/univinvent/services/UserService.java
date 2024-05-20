package univinvent.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import univinvent.dto.UserDTO;
import univinvent.entities.User;
import univinvent.repositories.UserRepository;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    //get list user
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    public List<User> getUserByUsernameOrFullName(String keyword) {
        return userRepository.getUserByUsernameOrFullName(keyword);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        if(user == null) throw new RuntimeException("Username not exist");
        return user;
    }

    public void delete(String username) {
        userRepository.deleteUserByUsername(username);
    }

    public void changePassword(UserDTO userDTO) {
        User user = userRepository.getUserByUsernameAndFullName(userDTO.getUsername(), userDTO.getFullName());
        if (user == null) {
            throw new RuntimeException("Account information is incorrect. Please try again.");
        }
        if (!encoder.matches(userDTO.getPassword(), user.getPassword()))
            throw new RuntimeException("Account information is incorrect. Please try again.");
        if(userDTO.getNewPassword() == null || !userDTO.getNewPassword().equals(userDTO.getConfirmPassword())
                || encoder.matches(userDTO.getNewPassword(), user.getPassword()))
            throw new RuntimeException("Password and confirm password must be the same");
        user.setPassword(encoder.encode(userDTO.getNewPassword()));
        userRepository.save(user);
    }
}
