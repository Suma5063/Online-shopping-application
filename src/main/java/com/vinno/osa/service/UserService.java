package com.vinno.osa.service;

import com.vinno.osa.dto.RegisterRequest;
import com.vinno.osa.dto.RegisterResponse;
import com.vinno.osa.entity.Role;
import com.vinno.osa.entity.User;
import com.vinno.osa.exception.CustomException;
import com.vinno.osa.repository.RoleRepository;
import com.vinno.osa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) throws CustomException {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomException("Email is already registered.");
        }

        // Create new User
        User user = new User();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        if (registerRequest.getAddress() != null) {
            user.setAddress(registerRequest.getAddress());
        }
        // Determine Role (Default to BUYER if null)
        String roleName = registerRequest.getRole() != null ? registerRequest.getRole().getRoleName().toUpperCase() : "BUYER";
        // Fetch Role from Database
        Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
        if (optionalRole.isEmpty()) {
            throw new CustomException("Role not found: " + roleName);
        }
        Role role = optionalRole.get();
        user.setRole(Collections.singleton(role)); // Assign Role to User
        User savedUser = userRepository.save(user); // Save User in Database

        // Send Welcome Email
        emailService.sendEmail(savedUser.getEmail(), "Welcome", "Thank you for registering!");
        String userId = savedUser.getUserId() != null ? savedUser.getUserId().toString() : null;
        return new RegisterResponse(
                userId,
                savedUser.getUsername(),
                savedUser.getName(), // Ensure name is correctly assigned
                savedUser.getEmail(), // Ensure email is correctly assigned
                role.getRoleName(),
                savedUser.getAddress(),
                "Registration successful" // Message field should have a relevant message
        );
    }


    // Helper method to check if the role is restricted
    private boolean isRestrictedRole(String roleName) {
        return "ADMIN".equalsIgnoreCase(roleName) || "SELLER".equalsIgnoreCase(roleName);
    }


    // Get a user by their email
    public Optional<User> getUserByEmail(String email) {
       return userRepository.findByEmail(email);
    }

    // Update user information
    public User updateUser(User user) {
        return userRepository.save(user);  // Save the updated user
    }

    public String deleteUser(User user){
        userRepository.deleteById(String.valueOf(user.getUserId()));
        return "User with "+user.getUserId()+" deleted successfully!";
    }

    /**
     * Deactivate user account by username.
     */
    public String deactivateUserByUsername(String username) throws CustomException {
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found"));

        user.setIsActive(false);
        userRepository.save(user);

        return "User account deactivated successfully";
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
