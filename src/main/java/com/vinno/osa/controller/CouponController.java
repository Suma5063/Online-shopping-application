package com.vinno.osa.controller;
import com.vinno.osa.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PutMapping("/apply")
    public ResponseEntity<String> applyCoupon(@RequestParam String orderId,
                                              @RequestParam String couponCode,
                                              @RequestParam BigDecimal orderTotal,
                                              @RequestParam String userId) {
        try {
            BigDecimal finalTotal = couponService.applyCoupon(couponCode, orderTotal, userId);
            return ResponseEntity.ok("Coupon applied successfully! New total: " + finalTotal);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
