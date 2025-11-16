package illinoistech.itm.web.system.integration.student_collabration_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for APIs (stateless, no cookies used for auth)
                .csrf(csrf -> csrf.disable())

                // Enable CORS using the configurationSource() bean
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // For REST APIs, stateless sessions are usually the right choice
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Allow all CORS preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints (no auth required)
                        .requestMatchers(
                                "/api/auth/signup",
                                "/api/auth/signin",
                                "/actuator/health",
                                "/actuator/health/**",
                                "/api/applications/{id}"
                        ).permitAll()

                        // TEMP: allow all project endpoints (GET/POST/PUT/DELETE) without auth
                        // so you can test from Postman and frontend easily
                        .requestMatchers("/api/projects/**").permitAll()

                        // Any other /api/auth/** endpoints require authentication
                        .requestMatchers("/api/auth/**").authenticated()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                // Allow anonymous access for the permitAll() endpoints
                .anonymous(Customizer.withDefaults())

                // Use HTTP Basic auth for protected endpoints (good enough for testing)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Allowed origins - adjust when you add more environments
        cfg.setAllowedOrigins(List.of(
                "http://localhost:8080",   // if backend serves something
                "http://localhost:5173",   // Vite dev server
                "https://dj3eozung04ja.cloudfront.net", // your CloudFront React app
                "https://api.iit-scp.click"             // production API domain
        ));

        // Allowed HTTP methods
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allowed request headers
        cfg.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // Headers exposed to the browser (frontend can read these)
        cfg.setExposedHeaders(List.of("Location"));

        // Allow credentials (cookies/auth headers) from allowed origins
        cfg.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
