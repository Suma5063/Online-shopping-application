package com.vinno.osa.controller;

import com.vinno.osa.dto.EmailRequest;
import com.vinno.osa.dto.OrderConfirmationRequest;
import com.vinno.osa.entity.OrderItem;
import com.vinno.osa.service.EmailService;
import com.vinno.osa.service.PromotionalEmailService;
import com.vinno.osa.service.TransactionalEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PromotionalEmailService promotionalEmailService;

    @Autowired
    private TransactionalEmailService transactionalEmailService;

    // Endpoint to trigger email sending
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        String email = emailRequest.getEmail();
        String subject = emailRequest.getSubject();
        String body = emailRequest.getBody();

        // Call the service to send the email
        emailService.sendEmail(email, subject, body);
        return "Email sent successfully to " + email;
    }

    // 1. Order Confirmation
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping("/transactional/order-confirmation")
    public ResponseEntity<String> sendOrderConfirmationEmail(@RequestBody OrderConfirmationRequest request) {
        if (request.getUserEmail() == null || request.getOrderId() == null || request.getItems() == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        StringBuilder itemsDetails = new StringBuilder();
        for (OrderItem item : request.getItems()) {
            itemsDetails.append(item.getProductName())
                    .append(" (Quantity: ").append(item.getQuantity())
                    .append(", Price: ").append(item.getPrice()).append("), ");
        }
        String itemsString = itemsDetails.toString();

        transactionalEmailService.sendOrderConfirmationEmail(
                request.getUserEmail(),
                request.getOrderId(),
                itemsString,  // Now sending the String representation
                request.getTotalCost(),
                request.getShippingAddress(),
                request.getTrackingLink()
        );
        return ResponseEntity.ok("Order confirmation email sent to " + request.getUserEmail());
    }

    // 2. Password Reset
    @PostMapping("/transactional/password-reset")
    public String sendPasswordResetEmail(@RequestParam String userEmail, @RequestParam String resetLink) {
        transactionalEmailService.sendPasswordResetEmail(userEmail, resetLink);
        return "Password reset email sent to " + userEmail;
    }

    // 3. Account Activation
    @PostMapping("/transactional/account-activation")
    public String sendAccountActivationEmail(@RequestParam String userEmail, @RequestParam String activationLink) {
        transactionalEmailService.sendAccountActivationEmail(userEmail, activationLink);
        return "Account activation email sent to " + userEmail;
    }

    // 4. Order Dispatch
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping("/transactional/order-dispatch")
    public String sendOrderDispatchEmail(@RequestParam String userEmail, @RequestParam String orderId, @RequestParam String trackingId, @RequestParam String expectedDeliveryDate) {
        transactionalEmailService.sendOrderDispatchEmail(userEmail, orderId, trackingId, expectedDeliveryDate);
        return "Order dispatch email sent to " + userEmail;
    }

    // 5. Order Delivered

    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping("/transactional/order-delivered")
    public String sendOrderDeliveredEmail(@RequestParam String userEmail, @RequestParam String orderId, @RequestParam String reviewLink) {
        transactionalEmailService.sendOrderDeliveredEmail(userEmail, orderId, reviewLink);
        return "Order delivered email sent to " + userEmail;
    }

    // 6. Payment Failure
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transactional/payment-failure")
    public String sendPaymentFailureEmail(@RequestParam String userEmail, @RequestParam String reason, @RequestParam String retryLink) {
        transactionalEmailService.sendPaymentFailureEmail(userEmail, reason, retryLink);
        return "Payment failure email sent to " + userEmail;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/transactional/order-cancellation")
    public String sendOrderCancellationEmail(@RequestParam String userEmail,
                                             @RequestParam String orderId,
                                             @RequestParam double refundAmount) {
        transactionalEmailService.sendOrderCancellationEmail(userEmail, orderId, refundAmount);
        return "Order cancellation email sent to " + userEmail;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/discounts")
    public ResponseEntity<String> sendPersonalizedDiscountEmails(@RequestParam String productName, @RequestParam double discount) {
        promotionalEmailService.sendPersonalizedDiscountEmails(productName, discount);
        return ResponseEntity.ok("Bulk personalized discount emails are being sent!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/seasonal-sale")
    public ResponseEntity<String> sendSeasonalSaleEmails(@RequestParam String saleEvent) {
        promotionalEmailService.sendSeasonalSaleEmails(saleEvent);
        return ResponseEntity.ok("Bulk seasonal sale emails are being sent!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/new-product")
    public ResponseEntity<String> sendNewProductLaunchEmails(@RequestParam String productName) {
        promotionalEmailService.sendNewProductLaunchEmails(productName);
        return ResponseEntity.ok("Bulk new product launch emails are being sent!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/abandoned-cart")
    public ResponseEntity<String> sendAbandonedCartEmails(@RequestParam String cartItems) {
        promotionalEmailService.sendAbandonedCartEmails(cartItems);
        return ResponseEntity.ok("Bulk abandoned cart reminder emails are being sent!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/referral")
    public ResponseEntity<String> sendReferralProgramEmails(@RequestParam String referralLink) {
        promotionalEmailService.sendReferralProgramEmails(referralLink);
        return ResponseEntity.ok("Bulk referral program emails are being sent!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/loyalty-points")
    public ResponseEntity<String> sendLoyaltyProgramUpdateEmails(@RequestParam int pointsBalance) {
        promotionalEmailService.sendLoyaltyProgramUpdateEmails(pointsBalance);
        return ResponseEntity.ok("Bulk loyalty program update emails are being sent!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/newsletter")
    public ResponseEntity<String> sendNewsletterEmails(@RequestParam String newsletterContent) {
        promotionalEmailService.sendNewsletterEmails(newsletterContent);
        return ResponseEntity.ok("Bulk newsletter emails are being sent!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotional/reengagement")
    public ResponseEntity<String> sendReEngagementEmails() {
        promotionalEmailService.sendReEngagementEmails();
        return ResponseEntity.ok("Bulk re-engagement emails are being sent!");
    }
}
    /*//Promotional Emails
    // 1. Personalized Discounts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/discount")
    public String sendPersonalizedDiscountEmail(@RequestParam String userEmail,
                                                @RequestParam String productName,
                                                @RequestParam double discount) {
        promotionalEmailService.sendPersonalizedDiscountEmail(userEmail, productName, discount);
        return "Personalized discount email sent to " + userEmail;
    }

    // 2. Seasonal Sales & Events
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/seasonal-sale")
    public String sendSeasonalSaleEmail(@RequestParam String userEmail, @RequestParam String saleEvent) {
        promotionalEmailService.sendSeasonalSaleEmail(userEmail, saleEvent);
        return "Seasonal sale email sent to " + userEmail;
    }

    // 3. New Product Launches
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/new-product")
    public String sendNewProductLaunchEmail(@RequestParam String userEmail, @RequestParam String productName) {
        promotionalEmailService.sendNewProductLaunchEmail(userEmail, productName);
        return "New product launch email sent to " + userEmail;
    }

    // 4. Abandoned Cart Reminder
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/abandoned-cart")
    public String sendAbandonedCartEmail(@RequestParam String userEmail, @RequestParam String cartItems) {
        promotionalEmailService.sendAbandonedCartEmail(userEmail, cartItems);
        return "Abandoned cart reminder email sent to " + userEmail;
    }

    // 5. Referral Program
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/referral")
    public String sendReferralProgramEmail(@RequestParam String userEmail, @RequestParam String referralLink) {
        promotionalEmailService.sendReferralProgramEmail(userEmail, referralLink);
        return "Referral program email sent to " + userEmail;
    }

    // 6. Loyalty Program Updates
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/loyalty-points")
    public String sendLoyaltyProgramUpdateEmail(@RequestParam String userEmail, @RequestParam int pointsBalance) {
        promotionalEmailService.sendLoyaltyProgramUpdateEmail(userEmail, pointsBalance);
        return "Loyalty program update email sent to " + userEmail;
    }

    // 7. Newsletter Subscription
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/newsletter")
    public String sendNewsletterEmail(@RequestParam String userEmail, @RequestParam String newsletterContent) {
        promotionalEmailService.sendNewsletterEmail(userEmail, newsletterContent);
        return "Newsletter email sent to " + userEmail;
    }

    // 8. Re-Engagement Campaign
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promotions/reengagement")
    public String sendReEngagementEmail(@RequestParam String userEmail) {
        promotionalEmailService.sendReEngagementEmail(userEmail);
        return "Re-engagement email sent to " + userEmail;
    }*/
