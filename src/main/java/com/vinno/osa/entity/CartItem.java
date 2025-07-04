package com.vinno.osa.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItem {

    private String productId;
    private int quantity;
    private BigDecimal price;
    private int stock;
    private String productName;

    public CartItem() {
    }

    public CartItem(String productId, int quantity, BigDecimal price) {
        this.productId=productId;
        this.quantity=quantity;
        this.price=price;
    }
}
