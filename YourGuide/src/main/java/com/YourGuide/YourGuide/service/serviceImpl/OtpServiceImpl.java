package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.service.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {
    @Override
    public String generateOtp() {
        int otp = 100000+new Random().nextInt(900000);
        return String.valueOf(otp);
    }
}
