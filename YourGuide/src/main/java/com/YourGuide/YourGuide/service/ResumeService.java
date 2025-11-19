package com.YourGuide.YourGuide.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResumeService {
    String uploadResume(MultipartFile file , String userEmail);
    String extractText(MultipartFile file) throws IOException;
}
