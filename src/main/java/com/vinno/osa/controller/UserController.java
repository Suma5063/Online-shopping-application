package com.vinno.osa.controller;
import com.vinno.osa.dto.RegisterResponse;
import com.vinno.osa.entity.Role;
import com.vinno.osa.entity.User;
import com.vinno.osa.exception.CustomException;
import com.vinno.osa.exception.UserNotFoundException;
import com.vinno.osa.service.RoleService;
import com.vinno.osa.service.UserService;
import com.vinno.osa.dto.RegisterRequest;
import com.vinno.osa.dto.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // ✅ Public Registration (only for BUYER)
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        // Ensure only 'BUYER' is allowed in public registration
        if (registerRequest.getRole() != null &&
                ("ADMIN".equalsIgnoreCase(registerRequest.getRole().getRoleName()) ||
                        "SELLER".equalsIgnoreCase(registerRequest.getRole().getRoleName()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RegisterResponse("Role '" + registerRequest.getRole().getRoleName() + "' cannot be used for public registration."));
        }

        // Proceed with user registration
        RegisterResponse response = userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //  Restricted Registration (Only ADMIN can register ADMIN or SELLER)
    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegisterResponse> registerAdminOrSeller(@RequestBody RegisterRequest registerRequest) {
        // Ensure only ADMIN can register SELLER or ADMIN
        if (!("ADMIN".equalsIgnoreCase(registerRequest.getRole().getRoleName()) ||
                "SELLER".equalsIgnoreCase(registerRequest.getRole().getRoleName()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new RegisterResponse("Only ADMIN can register ADMIN or SELLER."));
        }

        // Proceed with the registration
        RegisterResponse response = userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/details")
    public ResponseEntity<User> getUserDetails() {
        // Get the current authenticated user from SecurityContext
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();                   // Assuming username is email

        User user = userService.getUserByEmail(email)               // Find the user by email
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);  // Return user details
    }

    // Update user details
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();                            // Assuming username is email

        User existingUser = userService.getUserByEmail(email)         // Find the user by email
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setUsername(updateUserRequest.getName());             // Update user details
        existingUser.setEmail(updateUserRequest.getEmail());
        if (updateUserRequest.getPassword() != null) {
            existingUser.setPassword(updateUserRequest.getPassword());
        }
       User updatedUser = userService.updateUser(existingUser);        // Save the updated user
        return ResponseEntity.ok(updatedUser);
    }

    // Update User role
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-role")
    public String updateUserRole(@RequestParam String email, @RequestParam Role newRole) {
        return roleService.updateUserRole(email, newRole);
    }


    @DeleteMapping("/delete")
    public String deleteUser(User user){
        return userService.deleteUser(user);
    }


    @PreAuthorize("hasRole('BUYER')")
    @PutMapping("/deactivate")
    public String deactivateUserByUsername(String username) throws CustomException {
        return userService.deactivateUserByUsername(username);
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }

        return ResponseEntity.ok(user.get());
    }

}
