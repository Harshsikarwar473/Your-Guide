package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.service.EmailServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailServices {

   private final JavaMailSender mailSender;
    @Override
    public void sendOtpEmail(String email, String otp) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your Login Otp For Your Guide ");
            message.setText("Your Login Otp For Your Guide is : " + otp);
            mailSender.send(message);
        }catch (Exception e){
            log.error("Failed to send Otp email : {}" , e.getMessage());
        }


    }
}
