package illinoistech.itm.web.system.integration.student_collabration_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
//                .csrf(csrf -> csrf.disable())
                .csrf(AbstractHttpConfigurer::disable)

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
                                "/api/applications/{id}",
                                "/api/projects//industry/{industry_id}",
                                "/api/applications/project/{id}",
                                "/api/applications/apply",
                                "/api/applications/industry",
                                "/api/student/**",
                                "/api/applications/industry/project-applications",
                                "/api/projects/Industry/**",
                                "/api/projects/industry/**"

                        ).permitAll()

                        // Public applications & projects (for now)
                        .requestMatchers("/api/applications/**").permitAll()
                        // Industry and Students endpoints - must come before general /api/projects/**
                        .requestMatchers(HttpMethod.GET, "/api/projects/Industry/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/projects/industry/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/projects/students/**").permitAll()
                        .requestMatchers("/api/projects/**").permitAll()

                        // ⬇️ Student profile endpoints: make them PUBLIC for testing
                        .requestMatchers(HttpMethod.POST, "/api/student-profiles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/student-profiles/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/student-profiles/**").permitAll()

                        //Industry profile endpoint:
                        .requestMatchers(HttpMethod.GET, "/api/industry-profiles/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/industry-profiles/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/industry-profiles/**").permitAll()

                        // If you have other /api/auth/** that should be protected, add:
                        // .requestMatchers("/api/auth/**").authenticated()

                        // Everything else (not matched above) requires authentication
                        .anyRequest().authenticated()
                )

                // Allow anonymous for permitAll() endpoints
                .anonymous(Customizer.withDefaults());


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

//        cfg.setAllowedOriginPatterns(List.of("*"));

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

        // Headers exposed to the browser
        cfg.setExposedHeaders(List.of("Location"));

        // Allow credentials (cookies/auth headers) from allowed origins
//        cfg.setAllowCredentials(true);
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
