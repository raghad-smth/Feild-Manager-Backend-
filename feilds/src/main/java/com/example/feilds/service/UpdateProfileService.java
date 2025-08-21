package com.example.feilds.service;

import com.example.feilds.model.Users;
import com.example.feilds.repository.UpdateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileService {

    private final UpdateProfileRepository updateProfileRepository;

    public Users updateProfile(Integer userId, String name, String phone, String password) {
        // Validate user ID
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        // Find user
        Users user = updateProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        boolean hasUpdates = false;

        // Validate and update name
        if (name != null && !name.trim().isEmpty()) {
            if (name.trim().length() < 2) {
                throw new IllegalArgumentException("Name must be at least 2 characters long");
            }
            if (name.trim().length() > 100) {
                throw new IllegalArgumentException("Name must be less than 100 characters");
            }
            user.setName(name.trim());
            hasUpdates = true;
        }

        // Validate and update phone
        if (phone != null && !phone.trim().isEmpty()) {
            String cleanPhone = phone.trim().replaceAll("[^0-9]", "");
            if (cleanPhone.length() < 8 || cleanPhone.length() > 15) {
                throw new IllegalArgumentException("Phone number must be between 8-15 digits");
            }
            user.setPhone(cleanPhone);
            hasUpdates = true;
        }

        // Validate and update password
        if (password != null && !password.trim().isEmpty()) {
            if (password.trim().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters long");
            }
            if (password.trim().length() > 255) {
                throw new IllegalArgumentException("Password must be less than 255 characters");
            }
            user.setPassword(password.trim());
            hasUpdates = true;
        }

        // Check if any updates were made
        if (!hasUpdates) {
            throw new IllegalArgumentException("No valid fields provided for update");
        }

        return updateProfileRepository.save(user);
    }
}