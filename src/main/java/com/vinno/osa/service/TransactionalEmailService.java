package com.vinno.osa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class TransactionalEmailService {

    @Autowired
    private JavaMailSender mailSender;

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    //  Order Cancellation Email
    public void sendOrderCancellationEmail(String userEmail, String orderId, double refundAmount) {
        String subject = "Order Cancelled - " + orderId;
        String body = "Dear Customer,\n\nYour order " + orderId + " has been cancelled.\n"
                + "Refund Amount: $" + refundAmount + "\n\n"
                + "If you need assistance, contact support.";
        sendEmail(userEmail, subject, body);
    }

    //  Account Changes Email
    public void sendAccountChangeEmail(String userEmail, String changeType) {
        String subject = "Account Change Notification";
        String body = "Dear Customer,\n\nYour account " + changeType + " has been updated successfully.\n"
                + "If you did not authorize this change, please contact support immediately.\n\n"
                + "Stay secure!";
        sendEmail(userEmail, subject, body);
    }

    //  Wishlist Updates Email
    public void sendWishlistUpdateEmail(String userEmail, String productName, double newPrice) {
        String subject = "Wishlist Update: " + productName;
        String body = "Dear Customer,\n\nYour wishlist item '" + productName + "' is now available at $" + newPrice + ".\n"
                + "Hurry, stock is limited!\n\n"
                + "Click here to buy now.";
        sendEmail(userEmail, subject, body);
    }

    //  Order Confirmation Email
    public void sendOrderConfirmationEmail(String userEmail, String orderId, String items, double totalCost, String shippingAddress, String trackingLink) {
        String subject = "Order Confirmation - " + orderId;
        String body = "Dear Customer,\n\nThank you for your order!\n"
                + "Order ID: " + orderId + "\n"
                + "Items: " + items + "\n"
                + "Total Cost: $" + totalCost + "\n"
                + "Shipping Address: " + shippingAddress + "\n"
                + "Track your order here: " + trackingLink + "\n\n"
                + "Thank you for shopping with us!";
        sendEmail(userEmail, subject, body);
    }

    //  Password Reset Email
    public void sendPasswordResetEmail(String userEmail, String resetLink) {
        String subject = "Password Reset Request";
        String body = "Dear Customer,\n\nYou requested to reset your password. Click the link below:\n"
                + resetLink + "\n\n"
                + "This link is valid for a limited time.\n\n"
                + "If you didn’t request this, ignore this email.";
        sendEmail(userEmail, subject, body);
    }

    //  Account Activation Email
    public void sendAccountActivationEmail(String userEmail, String activationLink) {
        String subject = "Activate Your Account";
        String body = "Dear Customer,\n\nWelcome to our platform! Please activate your account by clicking the link below:\n"
                + activationLink + "\n\n"
                + "Enjoy shopping with us!";
        sendEmail(userEmail, subject, body);
    }

    //  Order Dispatch Notification
    public void sendOrderDispatchEmail(String userEmail, String orderId, String trackingId, String expectedDeliveryDate) {
        String subject = "Your Order is on the Way!";
        String body = "Dear Customer,\n\nYour order " + orderId + " has been shipped.\n"
                + "Tracking ID: " + trackingId + "\n"
                + "Expected Delivery Date: " + expectedDeliveryDate + "\n\n"
                + "Track your order here: [Tracking Link]\n\n"
                + "Thank you for shopping with us!";
        sendEmail(userEmail, subject, body);
    }

    //  Order Delivered Notification
    public void sendOrderDeliveredEmail(String userEmail, String orderId, String reviewLink) {
        String subject = "Your Order has been Delivered!";
        String body = "Dear Customer,\n\nYour order " + orderId + " has been successfully delivered.\n"
                + "We’d love to hear your feedback. Please leave a review:\n"
                + reviewLink + "\n\n"
                + "Thank you for shopping with us!";
        sendEmail(userEmail, subject, body);
    }

    //  Payment Failure Notification
    public void sendPaymentFailureEmail(String userEmail, String reason, String retryLink) {
        String subject = "Payment Failed - Please Try Again";
        String body = "Dear Customer,\n\nUnfortunately, your payment failed due to: " + reason + "\n"
                + "Please try again using this link:\n"
                + retryLink + "\n\n"
                + "If you need assistance, contact support.";
        sendEmail(userEmail, subject, body);
    }


}
