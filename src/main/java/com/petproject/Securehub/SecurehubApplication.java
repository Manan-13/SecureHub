package com.petproject.Securehub;

import com.petproject.Securehub.entity.User;
import com.petproject.Securehub.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SecurehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurehubApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserRepository userRepository) {
		return args -> {
			if (userRepository.findByUsername("mananAdmin").isEmpty()) {
				userRepository.save(new User(null, "mananAdmin", "pass123", Set.of("ROLE_ADMIN")));
			}
		};
	}
}
