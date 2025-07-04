package com.vinno.osa.service;

import com.vinno.osa.entity.GiftOrder;
import com.vinno.osa.repository.GiftOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class GiftOrderService {
    @Autowired
    private GiftOrderRepository giftOrderRepository;

    public GiftOrder createGiftOrder(GiftOrder giftOrder) {
        giftOrder.setCreatedAt(new Date());
        giftOrder.setUpdatedAt(new Date());
        if (giftOrder.getItems() == null || giftOrder.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create a gift order without items.");
        }
        BigDecimal total = giftOrder.getItems().stream()                              // Calculate total price including gift wrap charge
                .map(item -> (item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (giftOrder.isGiftWrap() && giftOrder.getGiftWrapCharge() != null) {       // Add gift wrap charge if applicable
            total = total.add(giftOrder.getGiftWrapCharge());
        }
        giftOrder.setTotalPrice(total);                                             // Set the correct total price
        return giftOrderRepository.save(giftOrder);                                 // Save the order
    }


    public Optional<GiftOrder> getGiftOrderById(String orderId) {
        return giftOrderRepository.findById(orderId);
    }

}
