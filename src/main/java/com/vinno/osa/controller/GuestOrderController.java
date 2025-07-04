package com.vinno.osa.controller;
import com.vinno.osa.entity.GuestOrder;
import com.vinno.osa.service.GuestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("guest-order")
public class GuestOrderController {

    @Autowired
    private GuestOrderService guestOrderService;

    @PostMapping("/create")
    public ResponseEntity<GuestOrder> createGuestOrder(@RequestBody GuestOrder guestOrder) {
        GuestOrder savedOrder = guestOrderService.createGuestOrder(guestOrder);
        return ResponseEntity.ok(savedOrder);
    }

    // ✅ No authentication required, but guests must provide email & order ID
    @GetMapping("/track")
    public ResponseEntity<GuestOrder> trackGuestOrder(@RequestParam String email, @RequestParam String orderId) {
        Optional<GuestOrder> order = guestOrderService.trackGuestOrder(email, orderId);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null)); // Return a null body with a bad request status
    }
}
