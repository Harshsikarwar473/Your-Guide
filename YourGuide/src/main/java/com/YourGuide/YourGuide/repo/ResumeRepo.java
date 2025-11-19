package com.YourGuide.YourGuide.repo;

import com.YourGuide.YourGuide.model.Resume;
import com.YourGuide.YourGuide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResumeRepo extends JpaRepository<Resume , Long> {
    Resume findByUser(User user);
}
