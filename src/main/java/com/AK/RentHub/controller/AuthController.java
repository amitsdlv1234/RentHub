package com.AK.RentHub.controller;

import com.AK.RentHub.model.AuthResponse;  // Import the new AuthResponse model
import com.AK.RentHub.model.User;
import com.AK.RentHub.security.JwtUtil;
import com.AK.RentHub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Register User with either email or mobile number
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        try {
            if (user.getEmail() == null) {
                return ResponseEntity.badRequest().body(new AuthResponse("Email must be provided.", null));
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(new AuthResponse("Password is required.", null));
            }
            User found = userService.findByEmail(user.getEmail());
            if (found != null) {
                return ResponseEntity.badRequest().body(new AuthResponse("Email ID is already registered.", null));
            }

            // Encrypt the password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Save the user in the database
            userService.registerUser(user);

            return ResponseEntity.ok(new AuthResponse("User registered successfully.", null));
        } catch (Exception e) {
            System.out.println("Internal Server Error :" + e);
            return ResponseEntity.status(500).body(new AuthResponse("Internal Server Error", null));
        }
    }

    // Login using either email or mobile number and password
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        try {
            User foundUser;
            if (user.getEmail() != null) {
                foundUser = userService.findByEmail(user.getEmail());
            } else if (user.getMobileNo() != null) {
                foundUser = userService.findByMobileNo(user.getMobileNo());
            } else {
                return ResponseEntity.badRequest().body(new AuthResponse("Email or Mobile number must be provided for login.", null));
            }
            if (foundUser == null) return ResponseEntity.badRequest().body(new AuthResponse("User not found", null));

            if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                String token = jwtUtil.generateToken(foundUser.getEmail());
                return ResponseEntity.ok(new AuthResponse("Login successful.", token));
            }
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials", null));
        } catch (Exception e) {
            System.out.println("Internal Server Error :" + e);
            return ResponseEntity.status(500).body(new AuthResponse("Internal Server Error", null));
        }
    }
}
