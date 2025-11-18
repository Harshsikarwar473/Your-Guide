package com.YourGuide.YourGuide.service;

import com.YourGuide.YourGuide.model.JobApplication;

import java.util.List;

public interface JobApplicationService {

    String applyJob(String email, Long jobId);
    List<JobApplication> getApplicationsForJob(Long jobId);
    List<JobApplication> getMyApplication(String email);
}
