package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Discount {

    @Id
    private String id;

    private String productId; // References Product

    private double discountValue; // Percentage or fixed amount

    private boolean isPercentage; // True if percentage discount

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean isActive;

    public Discount(String id, String productId, double discountValue,
                    boolean isPercentage, LocalDateTime startDate, LocalDateTime endDate, boolean isActive) {
        this.id = id;
        this.productId = productId;
        this.discountValue = discountValue;
        this.isPercentage = isPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Discount() {
    }



    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
