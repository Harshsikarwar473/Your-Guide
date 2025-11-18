package com.YourGuide.YourGuide.controller;


import com.YourGuide.YourGuide.model.JobApplication;
import com.YourGuide.YourGuide.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/application")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    // JOB SEEKER applies to job
    @PostMapping("/{jobId}")
    public ResponseEntity<String> applyToJob(
            @PathVariable Long jobId,
            Authentication auth
    ) {
        String email = auth.getName();
        String msg = jobApplicationService.applyJob(email, jobId);
        return ResponseEntity.ok(msg);
    }

    // EMPLOYER sees all applicants for their job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplication>> getApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobApplicationService.getApplicationsForJob(jobId));
    }

    // JOB SEEKER sees all his applications
    @GetMapping("/my")
    public ResponseEntity<List<JobApplication>> getMyApplications(Authentication auth) {
        return ResponseEntity.ok(jobApplicationService.getMyApplication(auth.getName()));
    }


}
