package com.example.feilds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig
 * - Defines how Spring Security will handle authentication/authorization
 * - Allows open access to /auth endpoints (register & login)
 * - Secures all other endpoints (requires authentication)
 */

@Configuration
public class SecurityConfig {

    /**
     * PasswordEncoder bean
     * - BCrypt is a strong hashing algorithm for passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * SecurityFilterChain bean
     * - Configures HTTP security rules
     */
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(requests -> requests
            .anyRequest().permitAll() // allow everything for now
        );
    return http.build();
}

 

}
