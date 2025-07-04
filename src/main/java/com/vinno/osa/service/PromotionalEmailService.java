package com.vinno.osa.service;

import com.vinno.osa.entity.User;
import com.vinno.osa.repository.UserRepository;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionalEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Async  // Sends emails asynchronously
    public void sendBulkEmails(String subject, String content) {
        List<User> buyers = userRepository.findAllBuyers();

        if (buyers.isEmpty()) {
            System.out.println("No buyers found for promotional emails.");
            return;
        }

        buyers.forEach(user -> sendEmail(user.getEmail(), subject, content));
    }

    // 1. Personalized Discounts
    public void sendPersonalizedDiscountEmails(String productName, double discount) {
        String subject = "Exclusive Discount Just for You!";
        String content = "Dear Customer,\n\nWe have an exclusive " + discount + "% discount on " + productName + "!\n"
                + "Limited-time offer. Don't miss out!\n\nClick here to grab your deal.";
        sendBulkEmails(subject, content);
    }

    // 2. Seasonal Sales & Events
    public void sendSeasonalSaleEmails(String saleEvent) {
        String subject = "Big Sale Alert: " + saleEvent;
        String content = "Dear Customer,\n\nOur " + saleEvent + " is now live! Enjoy massive discounts on your favorite products.\n"
                + "Limited stock available. Shop now before it's too late!\n\nClick here to shop the sale.";
        sendBulkEmails(subject, content);
    }

    // 3. New Product Launches
    public void sendNewProductLaunchEmails(String productName) {
        String subject = "Introducing Our New Arrival: " + productName;
        String content = "Dear Customer,\n\nWe are excited to introduce our latest product: " + productName + ".\n"
                + "Check out its amazing features and get yours today!\n\nClick here to explore now.";
        sendBulkEmails(subject, content);
    }

    // 4. Abandoned Cart Reminder
    public void sendAbandonedCartEmails(String cartItems) {
        String subject = "Your Cart is Waiting!";
        String content = "Dear Customer,\n\nYou left some items in your cart: " + cartItems + "\nDon't miss out on your favorite products!\n"
                + "Complete your purchase now and enjoy exclusive deals.\n\nClick here to return to your cart.";
        sendBulkEmails(subject, content);
    }

    // 5. Referral Program
    public void sendReferralProgramEmails(String referralLink) {
        String subject = "Invite Your Friends & Earn Rewards!";
        String content = "Dear Customer,\n\nShare your unique referral link and earn exciting rewards!\n"
                + "Your referral link: " + referralLink + "\n"
                + "Invite friends and get discounts on your next purchase.\n\nClick here to learn more.";
        sendBulkEmails(subject, content);
    }

    // 6. Loyalty Program Updates
    public void sendLoyaltyProgramUpdateEmails(int pointsBalance) {
        String subject = "Your Loyalty Points Update!";
        String content = "Dear Customer,\n\nYou currently have " + pointsBalance + " loyalty points in your account.\n"
                + "Redeem them for discounts and special rewards!\n\nClick here to check your rewards.";
        sendBulkEmails(subject, content);
    }

    // 7. Newsletter Subscription
    public void sendNewsletterEmails(String newsletterContent) {
        String subject = "Latest Updates & Shopping Tips!";
        String content = "Dear Customer,\n\nStay updated with our latest trends, exclusive deals, and shopping tips!\n\n"
                + newsletterContent + "\n\nClick here to read more.";
        sendBulkEmails(subject, content);
    }

    // 8. Re-Engagement Campaign
    public void sendReEngagementEmails() {
        String subject = "We Miss You! Here’s a Special Offer";
        String content = "Dear Customer,\n\nWe haven't seen you in a while!\n"
                + "Here's a special discount just for you. Come back and shop your favorite products!\n\nClick here to claim your discount.";
        sendBulkEmails(subject, content);
    }

    /*@Async  // Sends emails asynchronously to improve performance
    public void sendBulkPromotionalEmails(String subject, String content) {
        List<User> buyers = userRepository.findAllBuyers();  // Get all buyers

        if (buyers.isEmpty()) {
            System.out.println("No buyers found for promotional emails.");
            return;
        }
        buyers.forEach(user -> {
            sendEmail(user.getEmail(), subject, content);
        });
      //  System.out.println("Promotional emails sent successfully!");
    }

    // 1. Personalized Discounts
    public void sendPersonalizedDiscountEmail(String userEmail, String productName, double discount) {
        String subject = "Exclusive Discount Just for You!";
        String body = "Dear Customer,\n\nWe have an exclusive " + discount + "% discount on " + productName + "!\n"
                + "Limited-time offer. Don't miss out!\n\n"
                + "Click here to grab your deal.";
        sendEmail(userEmail, subject, body);
    }

    // 2. Seasonal Sales & Events
    public void sendSeasonalSaleEmail(String userEmail, String saleEvent) {
        String subject = "Big Sale Alert: " + saleEvent;
        String body = "Dear Customer,\n\nOur " + saleEvent + " is now live! Enjoy massive discounts on your favorite products.\n"
                + "Limited stock available. Shop now before it's too late!\n\n"
                + "Click here to shop the sale.";
        sendEmail(userEmail, subject, body);
    }

    // 3. New Product Launches
    public void sendNewProductLaunchEmail(String userEmail, String productName) {
        String subject = "Introducing Our New Arrival: " + productName;
        String body = "Dear Customer,\n\nWe are excited to introduce our latest product: " + productName + ".\n"
                + "Check out its amazing features and get yours today!\n\n"
                + "Click here to explore now.";
        sendEmail(userEmail, subject, body);
    }

    // 4. Abandoned Cart Reminder
    public void sendAbandonedCartEmail(String userEmail, String cartItems) {
        String subject = "Your Cart is Waiting!";
        String body = "Dear Customer,\n\nYou left some items in your cart: " + cartItems + "\nDon't miss out on your favorite products!\n"
                + "Complete your purchase now and enjoy exclusive deals.\n\n"
                + "Click here to return to your cart.";
        sendEmail(userEmail, subject, body);
    }

    // 5. Referral Program
    public void sendReferralProgramEmail(String userEmail, String referralLink) {
        String subject = "Invite Your Friends & Earn Rewards!";
        String body = "Dear Customer,\n\nShare your unique referral link and earn exciting rewards!\n"
                + "Your referral link: " + referralLink + "\n"
                + "Invite friends and get discounts on your next purchase.\n\n"
                + "Click here to learn more.";
        sendEmail(userEmail, subject, body);
    }

    // 6. Loyalty Program Updates
    public void sendLoyaltyProgramUpdateEmail(String userEmail, int pointsBalance) {
        String subject = "Your Loyalty Points Update!";
        String body = "Dear Customer,\n\nYou currently have " + pointsBalance + " loyalty points in your account.\n"
                + "Redeem them for discounts and special rewards!\n\n"
                + "Click here to check your rewards.";
        sendEmail(userEmail, subject, body);
    }

    // 7. Newsletter Subscription
    public void sendNewsletterEmail(String userEmail, String newsletterContent) {
        String subject = "Latest Updates & Shopping Tips!";
        String body = "Dear Customer,\n\nStay updated with our latest trends, exclusive deals, and shopping tips!\n\n"
                + newsletterContent + "\n\n"
                + "Click here to read more.";
        sendEmail(userEmail, subject, body);
    }

    // 8. Re-Engagement Campaign
    public void sendReEngagementEmail(String userEmail) {
        String subject = "We Miss You! Here’s a Special Offer";
        String body = "Dear Customer,\n\nWe haven't seen you in a while!\n"
                + "Here's a special discount just for you. Come back and shop your favorite products!\n\n"
                + "Click here to claim your discount.";
        sendEmail(userEmail, subject, body);
    }*/
}
