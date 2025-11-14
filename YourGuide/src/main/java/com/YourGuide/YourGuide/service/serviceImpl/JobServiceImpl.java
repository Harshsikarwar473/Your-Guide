package com.YourGuide.YourGuide.service.serviceImpl;

import com.YourGuide.YourGuide.model.Job;
import com.YourGuide.YourGuide.model.User;
import com.YourGuide.YourGuide.repo.JobRepo;
import com.YourGuide.YourGuide.repo.UserRepo;
import com.YourGuide.YourGuide.roles.Role;
import com.YourGuide.YourGuide.service.JobService;
import com.YourGuide.YourGuide.utils.GenerateJwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final GenerateJwtToken generateJwtToken;
    @Override
    public Job addJob(Job job, String email) throws Exception {
        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new Exception("Not a Valid User");
        }
        if(user.getRole()!= Role.EMPLOYER){
            throw new Exception("You can not Add jobs");

        }
        job.setEmployer(user);
        return jobRepo.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepo.findById(id).orElseThrow(()->new RuntimeException("job not found"));
    }

    @Override
    public List<Job> getJobByEmployer(String email) throws Exception {
        User user = userRepo.findByEmail(email);
        if(user==null||user.getRole()!=Role.EMPLOYER){
            throw new Exception("Invalid User");
        }
        return jobRepo.findByEmployer(user);
    }
}
