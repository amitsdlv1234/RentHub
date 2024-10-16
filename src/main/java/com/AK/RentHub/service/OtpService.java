package com.AK.RentHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private Map<String, String> otpData = new HashMap<>();

    @Autowired
    private EmailService emailService;

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        otpData.put(email, otp);

        // Send OTP to email
//        emailService.sendOtpEmail(email, otp);

        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        return otp.equals(otpData.get(email));
    }
}
