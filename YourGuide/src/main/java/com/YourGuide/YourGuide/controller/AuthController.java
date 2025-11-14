package com.YourGuide.YourGuide.controller;

import com.YourGuide.YourGuide.request.LoginRequest;
import com.YourGuide.YourGuide.request.SendOtpRequest;
import com.YourGuide.YourGuide.request.SignUpRequest;
import com.YourGuide.YourGuide.response.AuthResponse;
import com.YourGuide.YourGuide.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins="**")

public class AuthController {

    private final AuthService authService;


    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendOtpRequest request) {
        String msg = authService.sendOtp(request);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/logIn")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody LoginRequest request) throws Exception {
        AuthResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest request) throws Exception {
        String jwt = authService.createUser(request);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setToken(jwt);
        authResponse.setMessage("User Created Successfully");
        return ResponseEntity.ok(authResponse);
    }





}
