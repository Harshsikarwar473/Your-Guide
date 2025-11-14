package com.YourGuide.YourGuide.repo;

import com.YourGuide.YourGuide.model.Job;
import com.YourGuide.YourGuide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job , Long> {
    List<Job> findByEmployer(User user);
}
