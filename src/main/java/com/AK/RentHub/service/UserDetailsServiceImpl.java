package com.AK.RentHub.service;

import com.AK.RentHub.model.User;
import com.AK.RentHub.repository.UserRepository;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user with encoded password and set verification to false
    @Override
    public void registerUser(User user) {
         userRepository.save(user);
    }

    // Update user details, mainly used for updating the password
    @Override
    public void updateUser(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Find user by email
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Find user by mobile number
    @Override
    public User findByMobileNo(String mobileNo) {
        return userRepository.findByMobileNo(mobileNo).orElse(null);
    }

    // Mark the user as verified after OTP validation
    @Override
    public void activateUser(User user) {
        user.setIsVerified(true);  // Set the user as verified
        userRepository.save(user);  // Update the user record in the database
    }

    /**
     * Loads a user based on either email or mobile number. This method is required by Spring Security
     * to load user details during authentication.
     *
     * @param username can be either the email or the mobile number
     * @return UserDetails required by Spring Security
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find the user by email
        User user = userRepository.findByEmail(username).orElse(null);

        // If not found by email, try to find by mobile number
        if (user == null) {
            user = userRepository.findByMobileNo(username).orElseThrow(() ->
                    new UsernameNotFoundException("User not found with email or mobile number: " + username));
        }

        // Return Spring Security's User object with username and password
        return new org.springframework.security.core.userdetails.User(
                username, user.getPassword(), new ArrayList<>());
    }
}
