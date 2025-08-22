package com.example.feilds.controller;

import com.example.feilds.model.Users;
import com.example.feilds.service.UpdateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UpdateProfileController {

    private final UpdateProfileService updateProfileService;

    @PutMapping("/api/users/{userId}/profile")
    public ResponseEntity<?> updateProfile(
            @PathVariable Integer userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String password) {

        try {
            // Call service to update user

            Users updatedUser = updateProfileService.updateProfile(userId, name, phone, password);
            // Success response payload
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Profile updated successfully");
            response.put("data", updatedUser);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Handle validation errors (bad request)
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("errorCode", "VALIDATION_ERROR");
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (RuntimeException e) {
            // Handle case: User not found
            if (e.getMessage().contains("not found")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", e.getMessage());
                errorResponse.put("errorCode", "USER_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            // Handle unexpected errors (internal server error)
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "An unexpected error occurred");
            errorResponse.put("errorCode", "INTERNAL_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
