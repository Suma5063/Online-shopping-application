package com.vinno.osa.service;

import com.vinno.osa.entity.Coupon;
import com.vinno.osa.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public BigDecimal applyCoupon(String couponCode, BigDecimal orderTotal, String userId) {
        Optional<Coupon> couponOptional = couponRepository.findByCode(couponCode);

        if (couponOptional.isPresent()) {
            Coupon coupon = couponOptional.get();

            // Check if coupon is active and within valid date range
            if (!coupon.isActive() || LocalDateTime.now().isAfter(coupon.getValidTill())) {
                throw new RuntimeException("Coupon is expired or inactive");
            }

            // Check minimum order value condition
            if (orderTotal.compareTo(coupon.getMinOrderValue()) < 0) {
                throw new RuntimeException("Order value does not meet the minimum required for this coupon");
            }

            // Check if the coupon usage limit is reached
            if (coupon.getCurrentUsage() >= coupon.getTotalUsageLimit()) {
                throw new RuntimeException("This coupon has reached its maximum usage limit");
            }

            // Apply discount
            BigDecimal discountAmount;
            if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {
                discountAmount = orderTotal.multiply(coupon.getDiscountValue().divide(BigDecimal.valueOf(100)));
                discountAmount = discountAmount.min(coupon.getMaxDiscount()); // Ensure max discount limit
            } else {
                discountAmount = coupon.getDiscountValue();
            }
            coupon.setCurrentUsage(coupon.getCurrentUsage() + 1);       // Update coupon usage count
            couponRepository.save(coupon);
            return orderTotal.subtract(discountAmount);                 // Return new total after applying discount
        } else {
            throw new RuntimeException("Coupon not found");
        }
    }

}
