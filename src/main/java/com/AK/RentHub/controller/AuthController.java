package com.AK.RentHub.controller;

import com.AK.RentHub.model.User;
import com.AK.RentHub.security.JwtUtil;
import com.AK.RentHub.service.OtpService;
import com.AK.RentHub.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User foundUser = userService.findByEmail(user.getEmail());
        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok("Login successful, JWT token: " + token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // OTP functionalities for password update or forgotten password
    @PostMapping("/request-otp/forgot")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody User user) {
        User foundUser = userService.findByEmail(user.getEmail());

        if (foundUser != null) {
            // Generate the OTP
            String otp = otpService.generateOtp(foundUser.getEmail());
            System.out.println("OTP: " + otp);

            // Generate the OTP Token (used for OTP validation)
            String otpToken = jwtUtil.generateTokenForOtp(foundUser.getEmail(), otp);

            // Create a response map to return both tokens
            Map<String, String> response = new HashMap<>();
            response.put("otpToken", otpToken);   // Token for OTP validation

            return ResponseEntity.ok(response);
        }

        // If user not found, return error response
        return ResponseEntity.status(401).body(Collections.singletonMap("error", "Email not registered"));
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String otpToken, @RequestParam String otp) {
        try {
            // Extract claims from the JWT token (this includes the OTP and email)
            Claims claims = jwtUtil.extractClaims(otpToken);

            // Extract email and OTP from the token
            String tokenEmail = claims.getSubject();  // The subject is the email
            String tokenOtp = (String) claims.get("otp");  // The OTP is stored in claims

            // Validate the OTP
            if (tokenOtp.equals(otp)) {
                // OTP is correct, now generate a new JWT token to allow password update
                String passwordUpdateToken = jwtUtil.generateToken(tokenEmail);

                // Return the new token in the response, which will be used for updating the password
                return ResponseEntity.ok("OTP verified successfully. Use this token to update your password: " + passwordUpdateToken);
            } else {
                return ResponseEntity.status(401).body("Invalid OTP");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }


    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam String passwordUpdateToken, @RequestParam String newPassword) {
        try {
            // Extract the email from the password update token
            String email = jwtUtil.extractUserName(passwordUpdateToken);

            // Find the user by email
            User user = userService.findByEmail(email);
            System.out.println(user);

            if (user != null) {

                // Save the updated user to the database
                userService.updateUser(user,newPassword);

                return ResponseEntity.ok("Password updated successfully");
            }
            return ResponseEntity.status(404).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

}
