package com.YourGuide.YourGuide.service;

import com.YourGuide.YourGuide.request.LoginRequest;
import com.YourGuide.YourGuide.request.SendOtpRequest;
import com.YourGuide.YourGuide.request.SignUpRequest;
import com.YourGuide.YourGuide.response.AuthResponse;

public interface AuthService {
    public String sendOtp(SendOtpRequest req);
    public String createUser(SignUpRequest req) throws Exception;
    public AuthResponse verifyOtp(LoginRequest req) throws Exception;


}
