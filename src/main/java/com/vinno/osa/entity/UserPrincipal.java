package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Data
public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user) {
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
       // authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole())); // Assuming user roles are stored in the `role` field
        return authorities;
    }

    // Return the password (hashed)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Return the username (email or username)
    @Override
    public String getUsername() {
        return user.getEmail(); // Assuming the username is the email
    }

    // Check if the account is expired
    @Override
    public boolean isAccountNonExpired() {
        return true; // Can implement custom logic if needed
    }

    // Check if the account is locked
    @Override
    public boolean isAccountNonLocked() {
        return user.isActive(); // Assuming `active` is the status of the account
    }

    // Check if credentials (password) are expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Can implement custom logic if needed
    }

    // Check if the account is enabled
    @Override
    public boolean isEnabled() {
        return user.isActive(); // Assuming `active` status determines if the user is enabled
    }

    // Getter for the User entity
    public User getUser() {
        return user;
    }
}


