package org.example.repositorytesting;
/**
 *   @author Bohdan 
 *   @project repositorytesting
 *   @class RepositoryTestingApplication.java
 *   @version 1.0
 *   @since 4/24/2025 13:56
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.example.repositorytesting.model.User;
import org.example.repositorytesting.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RepositoryTestingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepositoryTestingApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository repository) {
        return args -> {
            if (repository.findAll().isEmpty()) {
                repository.save(User.builder()
                        .name("Marie Curie")
                        .code("Chemist")
                        .description("Famous scientist ###init")
                        .build());

                repository.save(User.builder()
                        .name("Nikola Tesla")
                        .code("Inventor")
                        .description("Electricity wizard ###init")
                        .build());

                repository.save(User.builder()
                        .name("Steve Wozniak")
                        .code("Engineer")
                        .description("Apple co-founder ###init")
                        .build());
            }
        };
    }
}