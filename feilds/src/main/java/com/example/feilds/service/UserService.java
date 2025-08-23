package com.example.feilds.service;

import com.example.feilds.model.Users;
import com.example.feilds.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service = Business Logic Layer
 * - Handles the logic of registering and authenticating users
 * - Uses UserRepository to talk to the database
 * - Uses PasswordEncoder to hash passwords securely
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection of dependencies
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user
     * - First check if the email already exists
     * - Encode (hash) the password before saving
     * - Save the user in DB
     */
    public Users registerUser(Users user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        return userRepository.save(user); // Save to DB
    }

    /**
     * Authenticate user (Login)
     * - Find user by email
     * - Compare entered password with stored hashed password
     */
    public Users authenticate(String email, String password) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Compare raw password with hashed password in DB
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user; // If OK, return the user
    }
}
