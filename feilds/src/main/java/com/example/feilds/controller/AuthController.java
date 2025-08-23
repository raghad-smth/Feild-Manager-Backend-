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
    public ResponseEntity<Users> login(@RequestBody Map<String, String> loginData) {
        Users user = userService.authenticate(
                loginData.get("email"),  // Get email from request body
                loginData.get("password") // Get password from request body
        );
        return ResponseEntity.ok(user);
    }
}
