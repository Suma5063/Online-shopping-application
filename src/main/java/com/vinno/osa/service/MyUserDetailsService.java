package com.vinno.osa.service;
import com.vinno.osa.entity.User;
import com.vinno.osa.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private  UserRepository repo;

    public MyUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

  /*  @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user with email: " + email);

        // Use orElseThrow to handle the absence of a user
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));

        System.out.println("User found: " + user.getEmail());

        // ✅ Convert roles properly and prefix them with "ROLE_"
        List<GrantedAuthority> authorities = user.getRole()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))  // ✅ Use getRoleName()
                .collect(Collectors.toList());


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)  // ✅ Assign correct authorities
                .build();
    } */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user with email: " + email);

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));

        System.out.println("User found: " + user.getEmail());

        // ✅ Convert Set<Role> to GrantedAuthority (adding "ROLE_" prefix)
        List<GrantedAuthority> authorities = user.getRole()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))  // Ensuring "ROLE_ADMIN", "ROLE_SELLER"
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

}
