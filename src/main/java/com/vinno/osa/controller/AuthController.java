package com.vinno.osa.controller;
import com.vinno.osa.dto.LoginResponse;
import com.vinno.osa.service.JwtService;

import com.vinno.osa.service.MyUserDetailsService;
import com.vinno.osa.dto.JwtResponse;
import com.vinno.osa.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.vinno.osa.utility.ErrorResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private  JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/loginUser")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Extract UserDetails from the Authentication object
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate the JWT token
            String token = jwtService.generateToken(userDetails);

            // Return the token in the response
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            // Return custom error response in case of authentication failure
            ErrorResponse errorResponse = new ErrorResponse("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest credentials) {
        try {
            // Load user details by email
            UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getEmail());

            // Check if the password matches
            if (!passwordEncoder.matches(credentials.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            // Generate JWT token
            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
