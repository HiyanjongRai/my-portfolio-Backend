// src/main/java/HiyanjongTilungRai/Resume/Security/JwtAuthFilter.java
package HiyanjongTilungRai.Resume.Security;

import HiyanjongTilungRai.Resume.Services.AppUserDetailsService;
import HiyanjongTilungRai.Resume.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final AppUserDetailsService uds;

    public JwtAuthFilter(JwtService jwt, AppUserDetailsService uds) {
        this.jwt = jwt;
        this.uds = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // Use servletPath to ignore any context-path prefixes automatically
        String path = request.getServletPath();

        // ✅ Public/ignored paths: CORS preflight + health probes
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.equals("/health")
                || path.equals("/api/health")
                || path.startsWith("/actuator/health")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String subject = jwt.extractSubject(token); // username
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = uds.loadUserByUsername(subject);
                    if (jwt.isValid(token, user.getUsername())) {
                        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception ignored) {
                // Optional: log at DEBUG to help diagnose bad tokens without breaking public endpoints
            }
        }

        chain.doFilter(request, response);
    }

}
