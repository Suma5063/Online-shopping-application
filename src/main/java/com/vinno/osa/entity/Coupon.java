package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "Coupons")
public class Coupon {

    @Id
    private String id;
    private String code;
    private String description;
    private String discountType;  // "PERCENTAGE" or "FLAT"
    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private List<String> applicableProducts; // Product IDs
    private List<String> applicableCategories; // Category names
    private BigDecimal maxDiscount;
    private LocalDateTime validFrom;
    private LocalDateTime validTill;
    private int usageLimitPerUser;
    private int totalUsageLimit;
    private int currentUsage;
    private boolean isActive;
    private boolean isExpired;
    private String type;

    public Coupon() {
    }


}
