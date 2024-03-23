package com.anae.api;

import com.anae.api.Entities.Role;
import com.anae.api.Enums.UserRole;
import com.anae.api.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByAuthority(UserRole.ADMIN).isPresent()) return;

            roleRepository.save(new Role(UserRole.ADMIN));
            roleRepository.save(new Role(UserRole.REGISTRATIONSERVMEMBER));
            roleRepository.save(new Role(UserRole.SECRETARY));
            roleRepository.save(new Role(UserRole.TEACHER));
            roleRepository.save(new Role(UserRole.STUDENT));
        };
    }

}
