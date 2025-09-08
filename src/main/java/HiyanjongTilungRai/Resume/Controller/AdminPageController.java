package HiyanjongTilungRai.Resume.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String adminPage(Authentication authentication) {
        if (authentication == null) {
            // Not logged in
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_Admin"));

        if (!isAdmin) {
            return "redirect:/login";
        }

        return "admin"; // admin.html will be served
    }
}
