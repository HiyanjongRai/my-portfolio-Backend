// src/main/java/HiyanjongTilungRai/Resume/Config/SecurityConfig.java
package HiyanjongTilungRai.Resume.Config;

import HiyanjongTilungRai.Resume.Security.JwtAuthFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c -> c.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // PUBLIC HEALTH
                        .requestMatchers(HttpMethod.GET, "/health", "/api/health").permitAll()

                        // PUBLIC VISITOR TRACKING (anonymous tracking)
                        .requestMatchers(HttpMethod.POST, "/api/visitors/track").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/visitors/count").permitAll()

                        // admin HTML (guard BEFORE static)
                        .requestMatchers(HttpMethod.GET, "/admin", "/admin.html", "/admin/**").hasRole("ADMIN")

                        // public root
                        .requestMatchers(HttpMethod.GET, "/", "/index.html", "gallery.html", "/favicon.ico", "/error")
                        .permitAll()

                        // auth endpoints public
                        .requestMatchers("/api/auth/**").permitAll()

                        // projects: public read, admin write
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasRole("ADMIN")

                        // images: public read, admin write
                        .requestMatchers(HttpMethod.GET, "/api/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/images/**").hasRole("ADMIN")

                        // static resources
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // the rest requires auth
                        .anyRequest().authenticated())

                // JWT filter first
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Different entry points (API -> 401 JSON, Admin -> redirect)
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                (req, res, e) -> res.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized"),
                                new AntPathRequestMatcher("/api/**"))
                        .defaultAuthenticationEntryPointFor(
                                (req, res, e) -> {
                                    res.setHeader(HttpHeaders.CACHE_CONTROL,
                                            "no-store, no-cache, must-revalidate, max-age=0");
                                    res.setHeader("Pragma", "no-cache");
                                    res.setDateHeader("Expires", 0);
                                    res.sendRedirect("/index.html");
                                },
                                new AntPathRequestMatcher("/admin"))
                        .defaultAuthenticationEntryPointFor(
                                (req, res, e) -> {
                                    res.setHeader(HttpHeaders.CACHE_CONTROL,
                                            "no-store, no-cache, must-revalidate, max-age=0");
                                    res.setHeader("Pragma", "no-cache");
                                    res.setDateHeader("Expires", 0);
                                    res.sendRedirect("/index.html");
                                },
                                new AntPathRequestMatcher("/admin/**")));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();

        // TODO: replace with your actual frontend(s)
        cfg.setAllowedOriginPatterns(List.of(
                "https://hiyanjong.vercel.app",
                "https://*.vercel.app",
                "http://localhost:*",
                "http://127.0.0.1:*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-CSRF-TOKEN"));
        cfg.setExposedHeaders(List.of("Content-Disposition", "Location", "X-Description"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
