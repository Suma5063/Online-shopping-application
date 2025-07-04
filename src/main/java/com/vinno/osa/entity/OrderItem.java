package com.vinno.osa.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {

    private String productId;
    private int quantity;
    private String productName;
    private BigDecimal price = BigDecimal.ZERO; // Default value

    public OrderItem(String productId, int quantity, BigDecimal price, String productName) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
    }

    public OrderItem() {
    }

}
