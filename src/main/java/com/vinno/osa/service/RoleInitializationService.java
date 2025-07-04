package com.vinno.osa.service;

import com.vinno.osa.entity.Role;
import com.vinno.osa.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleInitializationService implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> roles = Arrays.asList("BUYER", "ADMIN", "SELLER");

        for (String roleName : roles) {
            if (roleRepository.findByRoleName(roleName).isEmpty()) {  // Only add if it doesn't exist
                roleRepository.save(new Role(roleName));
                System.out.println(" Role added: " + roleName);
            }
        }
    }
}

