package com.vinno.osa.service;

import com.vinno.osa.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user) {
        // Replace with your email-sending logic
        System.out.println("Sending verification email to " + user.getEmail());
    }

    // Send order confirmation email
    public void sendOrderConfirmationEmail(String to, String orderId, String items, double totalCost, String shippingAddress, String deliveryDate, String trackingLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@vinno.com");
        message.setTo(to);
        message.setSubject("Order Confirmation - " + orderId);
        message.setText("Dear Customer,\n\nThank you for your order!\n\n" +
                "Order ID: " + orderId + "\n" +
                "Items: " + items + "\n" +
                "Total Cost: " + totalCost + "\n" +
                "Shipping Address: " + shippingAddress + "\n" +
                "Estimated Delivery Date: " + deliveryDate + "\n\n" +
                "You can track your order status here: " + trackingLink + "\n\n" +
                "Thank you for shopping with us!\n\nBest regards,\nVinno");

        mailSender.send(message);
    }

    // Send password reset email
    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@vinno.com");
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Dear User,\n\nWe received a request to reset your password.\n\n" +
                "To reset your password, please click the following link (the link will expire in 30 minutes):\n" +
                resetLink + "\n\n" +
                "If you did not request a password reset, please ignore this email.\n\nBest regards,\nVinno");

        mailSender.send(message);
    }

    // Method to send the email
    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true for HTML content
            // Optionally set the sender email if needed
            // helper.setFrom("sender@example.com");

            mailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendOrderConfirmation(String toEmail, String orderId, double totalPrice) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Order Confirmation - " + orderId);
        message.setText("Thank you for your order! \n\nOrder ID: " + orderId +
                "\nTotal Amount: $" + totalPrice +
                "\n\nYour order has been successfully placed.");

        mailSender.send(message);
    }
}
