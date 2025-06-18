package com.petproject.Securehub;

import com.petproject.Securehub.entity.User;
import com.petproject.Securehub.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecurehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurehubApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserRepository userRepository) {
		return args -> {
			if (userRepository.findByUsername("manan").isEmpty()) {
				userRepository.save(new User(null, "manan", "pass123", "ROLE_USER"));
			}
		};
	}
}
