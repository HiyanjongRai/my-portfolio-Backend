package HiyanjongTilungRai.Resume.Services;

import HiyanjongTilungRai.Resume.Model.Role;
import HiyanjongTilungRai.Resume.Model.User;
import HiyanjongTilungRai.Resume.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean authenticateUser(String usernameOrEmail, String rawPassword, Role requiredRole) {
        Optional<User> optionalUser = findByUsernameOrEmail(usernameOrEmail);
        if (optionalUser.isEmpty()) return false;
        User user = optionalUser.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) return false;

        if (requiredRole != null && user.getRole() != requiredRole) return false;

        return true;
    }


    public User createUser(String fullName, String username, String email, String rawPassword, Role role) {
        // Check duplicates
        if (userRepository.existsByUserName(username) || userRepository.existsByEmail(email)) {
            return null;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return userRepository.save(user);
    }


    public User createAdmin(String fullName, String username, String email, String rawPassword) {
        return createUser(fullName, username, email, rawPassword, Role.Admin);
    }

    // Convenience method to create a general User
    public User createGeneralUser(String fullName, String username, String email, String rawPassword) {
        return createUser(fullName, username, email, rawPassword, Role.User);
    }

    // Find user by username or email
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findByUserName(usernameOrEmail);
        if (user != null) return Optional.of(user);

        user = userRepository.findByEmail(usernameOrEmail);
        return Optional.ofNullable(user);
    }

    // Additional helper: check if username or email exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
