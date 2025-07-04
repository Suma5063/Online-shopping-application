package com.vinno.osa.controller;

import com.vinno.osa.entity.GiftOrder;
import com.vinno.osa.exception.CustomException;
import com.vinno.osa.service.GiftOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gift-order")
public class GiftOrderController {

    @Autowired
    private GiftOrderService giftOrderService;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/create")
    public ResponseEntity<GiftOrder> createGiftOrder(@RequestBody GiftOrder request) {
        GiftOrder giftOrder = giftOrderService.createGiftOrder(request);
        if (giftOrder == null) {
            throw new CustomException("Failed to create gift order.");
        }
        return ResponseEntity.ok(giftOrder);
    }


    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getGiftOrder(@PathVariable String orderId) {
        Optional<GiftOrder> order = giftOrderService.getGiftOrderById(orderId);

        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());  // Return the gift order if found
        } else {
            // Custom message when the gift order is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Gift order with ID " + orderId + " not found.");
        }
    }

}
