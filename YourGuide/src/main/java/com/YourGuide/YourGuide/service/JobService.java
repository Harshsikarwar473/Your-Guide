package com.YourGuide.YourGuide.service;

import com.YourGuide.YourGuide.model.Job;

import java.util.List;

public interface JobService {
     Job addJob(Job job , String email) throws Exception;
    List<Job> getAllJobs();
    Job getJobById(Long id);
    List<Job> getJobByEmployer(String email) throws Exception;

}
