package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.model.Resume;
import com.YourGuide.YourGuide.model.User;
import com.YourGuide.YourGuide.repo.ResumeRepo;
import com.YourGuide.YourGuide.repo.UserRepo;
import com.YourGuide.YourGuide.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepo resumeRepo;
    private final UserRepo userRepo;

    @Override
    public String uploadResume(MultipartFile file, String userEmail) {
        User user = userRepo.findByEmail(userEmail);
        if(user==null){
            throw new RuntimeException("User not found ");
        }

        String text = extractText(file);

        Resume resume= resumeRepo.findByUser(user);

        if(resume==null){
            resume=Resume.builder()
                    .user(user)
                    .fileName(file.getOriginalFilename())
                    .extractedText(text)
                    .build();
        }
        else{
            resume.setFileName(file.getOriginalFilename());
            resume.setExtractedText(text);
        }

        return "resume Uploaded Successfully";
    }

    @Override
    public String extractText(MultipartFile file)  {
        try{
            Tika tika = new Tika();
            return tika.parseToString(file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract resume text");
        }
    }
}
