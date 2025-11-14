package com.YourGuide.YourGuide;

import com.YourGuide.YourGuide.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.YourGuide.YourGuide.repo")
@EntityScan(basePackages = "com.YourGuide.YourGuide.model")

public class YourGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourGuideApplication.class, args);
	}

	@Bean
	public CommandLineRunner test(UserRepo userRepo) {
		return args -> {
			System.out.println("UserRepo bean = " + userRepo);
		};
	}

}
