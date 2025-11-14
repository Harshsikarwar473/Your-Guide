package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.model.User;
import com.YourGuide.YourGuide.model.Verification;
import com.YourGuide.YourGuide.repo.UserRepo;
import com.YourGuide.YourGuide.repo.VerificationRepo;
import com.YourGuide.YourGuide.request.LoginRequest;
import com.YourGuide.YourGuide.request.SendOtpRequest;
import com.YourGuide.YourGuide.request.SignUpRequest;
import com.YourGuide.YourGuide.response.AuthResponse;
import com.YourGuide.YourGuide.service.AuthService;
import com.YourGuide.YourGuide.service.EmailServices;
import com.YourGuide.YourGuide.service.OtpService;
import com.YourGuide.YourGuide.utils.GenerateJwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private  final EmailServices emailServices;
    private final OtpService otpService;
    private final UserRepo userRepo;
    private final GenerateJwtToken generateJwtToken;
    private final VerificationRepo verificationRepo;
    @Override
    public String sendOtp(SendOtpRequest req) {
        String otp = otpService.generateOtp();

        Verification verification = verificationRepo.findByEmail(req.getEmail());
        if(verification==null){
         verification=  Verification.builder()
                 .email(req.getEmail())
                 .role(req.getRole())
                 .build();
        }
        verification.setOtp(otp);
        verificationRepo.save(verification);



        emailServices.sendOtpEmail(req.getEmail() , otp);
        return "Otp sent Successfully";
    }

    @Override
    public String createUser(SignUpRequest req) throws Exception {

       Verification verification = verificationRepo.findByEmail(req.getEmail());
       if(verification==null||!verification.getOtp().equals(req.getOtp())){
           throw new Exception("wrong otp...");

       }

       User user = userRepo.findByEmail(req.getEmail());
       if(user==null){
           User newUser = new User();
           newUser.setName(req.getName());
           newUser.setEmail(req.getEmail());
           newUser.setRole(req.getRole());
           userRepo.save(newUser);
       }
       verificationRepo.delete(verification);

       return generateJwtToken.generateToken(req.getEmail() , req.getRole().name());







    }

    @Override
    public AuthResponse verifyOtp(LoginRequest req) throws Exception {
        Verification verification = verificationRepo.findByEmail(req.getEmail());
        if(verification==null||!verification.getOtp().equals(req.getOtp())){
            throw new Exception("wrong otp...");

        }

        User user = userRepo.findByEmail(req.getEmail());
        if(user==null){
            throw new Exception("User not found");
        }

        String token = generateJwtToken.generateToken(user.getEmail() , user.getRole().name());

        verificationRepo.delete(verification);


        return  AuthResponse.builder()
                .message("Login Successfully")
                .token(token).build();


    }
}
