package com.example.feilds.controller;

import com.example.feilds.model.Users;
import com.example.feilds.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth") // Base path: all endpoints here start with /auth
public class AuthController {

    private final UserService userService;


    // Constructor injection of service
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /auth/register
     * - Request body contains new user details (name, email, password, etc.)
     * - Calls UserService.registerUser()
     * - Returns saved user
     */
    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    /**
     * POST /auth/login
     * - Request body contains {"email": "...", "password": "..."}
     * - Calls UserService.authenticate()
     * - Returns the user if login is successful
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody Map<String, String> loginData) {
        Users user = userService.authenticate(
                loginData.get("email"),  // Get email from request body
                loginData.get("password") // Get password from request body
        );
        UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.ok(responseDTO);
    }
    // DTO for user response (excluding sensitive fields)
    public static class UserResponseDTO {
        private Long id;
        private String name;
        private String email;
        // Add other non-sensitive fields as needed

        public UserResponseDTO(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }
}
