package com.vinno.osa.service;
import com.vinno.osa.entity.GuestOrder;
import com.vinno.osa.entity.OrderItem;
import com.vinno.osa.repository.GuestOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GuestOrderService {

    @Autowired
    private GuestOrderRepository guestOrderRepository;

    @Autowired
    private EmailService emailService;

//    public GuestOrder createGuestOrder(GuestOrder guestOrder) {
//        guestOrder.setStatus("PENDING");
//        guestOrder.setCreatedAt(LocalDateTime.now());
//        guestOrder.setUpdatedAt(LocalDateTime.now());
//
//        // Calculate total price
//        BigDecimal total = BigDecimal.ZERO;
//        for (OrderItem item : guestOrder.getItems()) {
//            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
//        }
//
//        guestOrder.setTotalPrice(total.doubleValue());
//        GuestOrder savedOrder = guestOrderRepository.save(guestOrder);
//
//        // Send order confirmation email
//        emailService.sendOrderConfirmation(savedOrder.getEmail(), savedOrder.getId(), savedOrder.getTotalPrice());
//
//        return savedOrder;
//    }

    public GuestOrder createGuestOrder(GuestOrder guestOrder) {
        guestOrder.setStatus("PENDING");

        guestOrder.setCreatedAt(LocalDateTime.now());
        guestOrder.setUpdatedAt(LocalDateTime.now());

        // Calculate total price
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : guestOrder.getItems()) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        guestOrder.setTotalPrice(total.doubleValue());
        GuestOrder savedOrder = guestOrderRepository.save(guestOrder);

        // Send order confirmation email
        emailService.sendOrderConfirmation(savedOrder.getEmail(), savedOrder.getId(), savedOrder.getTotalPrice());

        return savedOrder;
    }


    // Track guest order
    public Optional<GuestOrder> trackGuestOrder(String email, String orderId) {
        return guestOrderRepository.findByEmailAndId(email, orderId);
    }

}
