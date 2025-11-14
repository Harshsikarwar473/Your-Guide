package com.YourGuide.YourGuide.controller;

import com.YourGuide.YourGuide.model.Job;
import com.YourGuide.YourGuide.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJobHandler(
            @RequestBody Job job  ,
            Authentication jwt
    ) throws Exception {
        String email = jwt.getName();
        Job saved =jobService.addJob(job , email);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs(){
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobBYId(@PathVariable Long id){
       return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/my-job")
    public ResponseEntity<List<Job>> getMyJob(Authentication jwt) throws Exception {
        String email = jwt.getName();
        return ResponseEntity.ok(jobService.getJobByEmployer(email));
    }
}

