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
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // For APIs, stateless is usually better. Remove if you rely on server sessions.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public endpoints - explicitly allow POST to create
                        .requestMatchers(
                                "/api/auth/signup",
                                "/api/auth/signin",
                                "/actuator/health",
                                "/actuator/health/**",
                                "/api/projects/all",
                                "/api/projects/create"
                                // add "/api/auth/home" here ONLY if you want it public:
                                // , "/api/auth/home"
                        ).permitAll()
                        
                        // Explicitly allow POST method for create endpoint
                        .requestMatchers(HttpMethod.POST, "/api/projects/create").permitAll()

                        // Everything else under /api/auth requires authentication
                        .requestMatchers("/api/auth/**").authenticated()

                        // Any other endpoints require auth
                        .anyRequest().authenticated()
                )
                // Allow anonymous access for public endpoints
                .anonymous(Customizer.withDefaults())
                // HTTP Basic auth for protected endpoints
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Exact origins you want to allow
        cfg.setAllowedOrigins(List.of(
                "http://localhost:8080",
                "http://localhost:5173",
                "https://dj3eozung04ja.cloudfront.net",
                "https://api.iit-scp.click"
        ));

        // Allowed methods
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allowed request headers (names only, not values)
        cfg.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // Expose response headers your frontend needs to read
        cfg.setExposedHeaders(List.of("Location"));

        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L); // cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
