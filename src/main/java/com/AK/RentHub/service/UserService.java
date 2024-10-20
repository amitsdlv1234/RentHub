package com.AK.RentHub.service;

import com.AK.RentHub.model.User;

public interface UserService {

    // Method to register a user
    void registerUser(User user);

    // Method to update user password or other details
    void updateUser(User user, String newPassword);

    // Method to find user by email
    User findByEmail(String email);

    // Method to find user by mobile number
    User findByMobileNo(String mobileNo);

    // Method to verify OTP and activate user
    void activateUser(User user);
}
