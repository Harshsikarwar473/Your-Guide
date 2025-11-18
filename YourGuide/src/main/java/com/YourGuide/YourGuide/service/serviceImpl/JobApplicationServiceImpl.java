package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.model.Job;
import com.YourGuide.YourGuide.model.JobApplication;
import com.YourGuide.YourGuide.model.User;
import com.YourGuide.YourGuide.repo.JobApplicationRepo;
import com.YourGuide.YourGuide.repo.JobRepo;
import com.YourGuide.YourGuide.repo.UserRepo;
import com.YourGuide.YourGuide.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {
    private final UserRepo userRepo;
    private final JobRepo jobRepo;
    private final JobApplicationRepo jobApplicationRepo;

    @Override
    public String applyJob(String email, Long jobId) {
        User user = userRepo.findByEmail(email);


        if (user == null) throw new RuntimeException("User not found");

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        List<JobApplication> existing = jobApplicationRepo.findByApplicant(user);
        for(JobApplication app :existing){
            if(app.getJob().getId().equals(jobId)){
                return "you already applied to this job";
            }
        }
        JobApplication application = JobApplication.builder()
                .applicant(user)
                .job(job)
                .status("APPLIED")
                .appliedAt(LocalDateTime.now())
                .resumeText("Later")
                .build();

        jobApplicationRepo.save(application);
        return "Applied successfully";
    }

    @Override
    public List<JobApplication> getApplicationsForJob(Long jobId) {
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return jobApplicationRepo.findByJob(job);
    }

    @Override
    public List<JobApplication> getMyApplication(String email) {
        User user = userRepo.findByEmail(email);
        return jobApplicationRepo.findByApplicant(user);
    }
}
