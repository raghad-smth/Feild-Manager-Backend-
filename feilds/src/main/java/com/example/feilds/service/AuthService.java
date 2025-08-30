package com.example.feilds.service;

import com.example.feilds.model.Users;
import com.example.feilds.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Sign in with email and password
     */
    public Users signIn(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Optional<Users> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Users user = userOpt.get();
        
        // Simple password check (in production, use proper password hashing)
        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Remove password from response for security
        user.setPassword(null);
        return user;
    }

    /**
     * Sign up with name, email, phone
     */
    public Users signUp(String name, String email, String phone, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        // Check if email already exists
        Optional<Users> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Validate email format
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Create new user (default role is player)
        Users newUser = Users.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .password(password) // In production, hash the password
                .role(Users.Role.player)
                .build();

        Users savedUser = userRepository.save(newUser);
        
        // Remove password from response for security
        savedUser.setPassword(null);
        return savedUser;
    }

    /**
     * Reset password (for now just validate email exists)
     */
    public void resetPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        Optional<Users> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Email not found");
        }

        // In a real implementation, you would:
        // 1. Generate a reset token
        // 2. Store it in database with expiration
        // 3. Send email with reset link
        
        // For now, just validate that the email exists
    }

    /**
     * Change password for authenticated user
     */
    public void changePassword(Integer userId, String currentPassword, String newPassword) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Current password cannot be empty");
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long");
        }

        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = userOpt.get();
        
        // Verify current password
        if (!currentPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Update password
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    /**
     * Basic email validation
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Get user by ID (for verification purposes)
     */
    public Users getUserById(Integer userId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        Users user = userOpt.get();
        // Remove password for security
        user.setPassword(null);
        return user;
    }
} 