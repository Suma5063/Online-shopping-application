package com.vinno.osa.service;

import com.vinno.osa.entity.Role;
import com.vinno.osa.entity.User;
import com.vinno.osa.repository.RoleRepository;
import com.vinno.osa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public String updateUserRole(String email, Role newRole) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();  // Get the User if present

            // Update the role
            user.setRole(Set.of(newRole));
            userRepository.save(user);  // Save the updated user

            return "User role updated successfully";  // Return success message
        } else {
            // Handle user not found
            throw new UsernameNotFoundException("User not found for email: " + email);
        }
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public void assignRoleToUser(User user, String roleName) {
        Role role = getRoleByName(roleName);
        user.setRole(Collections.singleton(role));
    }
}