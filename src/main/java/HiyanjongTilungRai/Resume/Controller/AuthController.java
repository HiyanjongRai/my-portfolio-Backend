package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Model.Role;
import HiyanjongTilungRai.Resume.Model.User;
import HiyanjongTilungRai.Resume.Repository.UserRepository;
import HiyanjongTilungRai.Resume.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtils;

    // Sign in
    @PostMapping("/signin")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("username"),
                        loginRequest.get("password")
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("username",
                userDetails.getUsername());
        return response;
    }

    // Sign up
    @PostMapping("/signup")
    public Map<String, String> registerUser(@RequestBody Map<String, String> signupRequest) {
        String username = signupRequest.get("username");
        String email = signupRequest.get("email");
        String fullName = signupRequest.get("fullName");
        String password = signupRequest.get("password");

        Map<String, String> response = new HashMap<>();

        if (userRepository.existsByUserName(username)) {
            response.put("error", "Username is already taken!");
            return response;
        }
        if (userRepository.existsByEmail(email)) {
            response.put("error", "Email is already in use!");
            return response;
        }

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUserName(username);
        newUser.setEmail(email);
        newUser.setPassword(encoder.encode(password));
        newUser.setRole(Role.Admin);

        userRepository.save(newUser);

        response.put("message", "User registered successfully!");
        return response;
    }
}
