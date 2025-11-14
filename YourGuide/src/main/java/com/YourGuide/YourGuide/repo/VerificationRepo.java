package com.YourGuide.YourGuide.repo;

import com.YourGuide.YourGuide.model.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepo extends JpaRepository<Verification , Long> {
    Verification findByEmail(String email);
}
