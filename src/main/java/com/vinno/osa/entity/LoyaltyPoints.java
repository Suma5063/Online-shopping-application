package com.vinno.osa.entity;

import lombok.Data;

@Data
public class LoyaltyPoints {

    private String customerName;
    private int pointsBalance;

    public LoyaltyPoints(String customerName) {
        this.customerName = customerName;
        this.pointsBalance = 0;
    }

    public LoyaltyPoints() {
    }

}
