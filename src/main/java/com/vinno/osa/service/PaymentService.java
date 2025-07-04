package com.vinno.osa.service;

import com.vinno.osa.entity.OrderStatus;
import com.vinno.osa.entity.Orders;
import com.vinno.osa.entity.PaymentStatus;
import com.vinno.osa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderRepository orderRepository;

    public void sendPaymentFailureEmail(String email, String orderId, String failureReason) {
        String subject = "Payment Failed for Order " + orderId;

        String body = "Unfortunately, your payment for Order ID " + orderId + " was not successful.\n\n" +
                "Reason: " + failureReason + "\n\n" +
                "Please retry your payment or use an alternative payment method.\n\n" +
                "Retry Payment: http://aa.com/retry-payment?orderId=" + orderId;

        emailService.sendEmail(email, subject, body);
    }

    // Method to handle payment failure
    public void handlePaymentFailure(String orderId, String failureReason) {
        Optional<Orders> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Orders order = optionalOrder.get();
            String email = order.getEmail();

            sendPaymentFailureEmail(email, orderId, failureReason);     // Send failure email

          //  order.setStatus(PaymentStatus.FAILED);                      // Update order status to 'Payment Failed'
            orderRepository.save(order);
        }
    }
}
