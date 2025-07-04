package com.vinno.osa.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationJwtFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationJwtFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
    }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(auth -> auth

                      .requestMatchers("/users/login", "/users/register").permitAll()
                      .requestMatchers("/api/mails/password-reset", "/api/mails/transactional/account-activation").permitAll()
                      .requestMatchers("/products/add").hasAnyRole("ADMIN", "SELLER")
                      .requestMatchers("/products/**").hasAnyRole("ADMIN", "BUYER")
                      .requestMatchers("/api/mails/send-email").hasRole("ADMIN")
                      .requestMatchers("/api/mails/transactional/**").hasAnyRole("ADMIN", "SELLER")
                      .requestMatchers("/api/mails/promotions/**").hasRole("ADMIN")
                      .requestMatchers("/api/wishlist/view").hasAnyRole("BUYER","ADMIN")
                      .requestMatchers("/api/wishlist/**").hasRole("BUYER")
                      .requestMatchers("/api/orders/place").hasRole("BUYER")
                      .requestMatchers("/api/orders/update/**").authenticated()
                      .requestMatchers("/api/orders/**").authenticated()
                      .requestMatchers("/guest-order/**").permitAll()
                      .anyRequest().authenticated()
              )
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authenticationProvider(authenticationProvider)
              .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
  }

}
