package com.YourGuide.YourGuide.repo;

import com.YourGuide.YourGuide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User , Long> {
    User findByEmail(String email);
}
