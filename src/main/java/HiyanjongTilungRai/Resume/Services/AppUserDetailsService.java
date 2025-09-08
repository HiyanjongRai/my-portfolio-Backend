package HiyanjongTilungRai.Resume.Services;

import HiyanjongTilungRai.Resume.Model.User;
import HiyanjongTilungRai.Resume.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public AppUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User u = users.findByUserNameIgnoreCaseOrEmailIgnoreCase(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + usernameOrEmail));

        // normalize -> ROLE_ADMIN or ROLE_USER
        String upper = u.getRole().name().toUpperCase();
        String authority = upper.startsWith("ROLE_") ? upper : "ROLE_" + upper;

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUserName())
                .password(u.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(authority)))
                .accountExpired(false).accountLocked(false)
                .credentialsExpired(false).disabled(false)
                .build();
    }
}
