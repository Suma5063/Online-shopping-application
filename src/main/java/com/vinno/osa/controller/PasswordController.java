package com.vinno.osa.controller;
import com.vinno.osa.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/reset")
    public String resetPassword(@RequestParam String email, @RequestParam String resetLink) {

        // Call email service to send password reset email
        emailService.sendPasswordResetEmail(email, resetLink);

        return "Password reset email has been sent.";
    }
}
