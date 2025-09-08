package HiyanjongTilungRai.Resume.Controller;

import HiyanjongTilungRai.Resume.Model.Role;
import HiyanjongTilungRai.Resume.Model.User;
import HiyanjongTilungRai.Resume.Repository.UserRepository;
import HiyanjongTilungRai.Resume.Services.JwtService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "*") // helpful for local HTML files; tighten in prod
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwt;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(
            AuthenticationManager authManager,
            JwtService jwt,
            UserRepository users,
            PasswordEncoder encoder
    ) {
        this.authManager = authManager;
        this.jwt = jwt;
        this.users = users;
        this.encoder = encoder;
    }

    /** -------- Register (signup) -------- */
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (users.findByUserNameIgnoreCaseOrEmailIgnoreCase(req.userName(), req.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "username or email exists"));
        }
        User u = new User();
        u.setFullName(req.fullName());
        u.setUserName(req.userName());
        u.setEmail(req.email());
        u.setPassword(encoder.encode(req.password()));
        u.setRole(req.role() != null ? req.role() : Role.USER);
        users.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + u.getId()))
                .body(Map.of("message", "registered"));
    }

    /** -------- Sign in (matches main.js) -------- */
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody SignInRequest req) {
        // Accept username or email in the "username" field
        String principal = req.username();
        Optional<User> uOpt = users.findByUserNameIgnoreCaseOrEmailIgnoreCase(principal, principal);

        // Authenticate via Spring Security
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(principal, req.password())
        );

        // Build token for the canonical username
        String usernameForToken = uOpt.map(User::getUserName).orElse(auth.getName());
        String token = jwt.generate(usernameForToken, Map.of("scope", "api"));

        Role role = uOpt.map(User::getRole).orElse(Role.USER);
        return ResponseEntity.ok(new AuthResponse(token, usernameForToken, role.name()));
    }

    /** -------- Optional: whoami for quick checks -------- */
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails user) {
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Optional<User> uOpt = users.findByUserNameIgnoreCaseOrEmailIgnoreCase(user.getUsername(), user.getUsername());
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "role", uOpt.map(User::getRole).map(Enum::name).orElse("USER")
        ));
    }

    /** -------- DTOs -------- */
    public record RegisterRequest(
            @NotBlank String fullName,
            @NotBlank String userName,
            @NotBlank String email,
            @NotBlank String password,
            Role role
    ) {}

    // matches frontend body: { "username": "...", "password": "..." }
    public record SignInRequest(@NotBlank String username, @NotBlank String password) {}

    public record AuthResponse(String token, String username, String role) {}

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Find the matching user from DB so you can get fullName too
        Optional<User> uOpt = users.findByUserNameIgnoreCaseOrEmailIgnoreCase(user.getUsername(), user.getUsername());

        return ResponseEntity.ok(Map.of(
                "username", uOpt.map(User::getUserName).orElse(user.getUsername()),
                "fullName", uOpt.map(User::getFullName).orElse(""),
                "email",    uOpt.map(User::getEmail).orElse(""),
                "role",     uOpt.map(User::getRole).map(Enum::name).orElse("USER")
        ));
    }

}
