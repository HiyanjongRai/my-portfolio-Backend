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
                // stateless API with JWT
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(c -> c.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // always allow preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // *** PROTECT ADMIN HTML FIRST (before static resources) ***
                        .requestMatchers(HttpMethod.GET, "/admin", "/admin.html", "/admin/**").hasRole("ADMIN")

                        // public root pages
                        .requestMatchers(HttpMethod.GET, "/", "/index.html", "/favicon.ico", "/error").permitAll()

                        // auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // projects: public read, admin writes
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasRole("ADMIN")

                        // images: public read, admin writes
                        .requestMatchers(HttpMethod.GET, "/api/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/images/**").hasRole("ADMIN")

                        // *** static resources (placed AFTER admin rule) ***
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // anything else requires auth
                        .anyRequest().authenticated()
                )

                // Attach JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Different entry points for APIs vs admin HTML
                .exceptionHandling(ex -> ex
                        // APIs -> 401 JSON
                        .defaultAuthenticationEntryPointFor(
                                (req, res, e) -> res.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized"),
                                new AntPathRequestMatcher("/api/**")
                        )
                        // Admin HTML -> redirect and ensure no caching
                        .defaultAuthenticationEntryPointFor(
                                (req, res, e) -> {
                                    res.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0");
                                    res.setHeader("Pragma", "no-cache");
                                    res.setDateHeader("Expires", 0);
                                    res.sendRedirect("/index.html");
                                },
                                new AntPathRequestMatcher("/admin**")
                        )
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();

        // Allow your frontend(s)
        cfg.setAllowedOriginPatterns(List.of(
                "https://YOUR-VERCEL-PROJECT.vercel.app", // exact Vercel deployment
                "https://hiyanjong.vercel.app",                   // any preview on Vercel (optional)
                "http://localhost:*",                     // local dev (any port)
                "http://127.0.0.1:*",                     // local dev (any port)
                "null"                                    // file:// usage (optional; remove if not needed)
        ));

        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept","X-CSRF-TOKEN"));
        cfg.setExposedHeaders(List.of("Content-Disposition","Location","X-Description"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
