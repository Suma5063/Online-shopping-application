package com.vinno.osa.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

public class WishlistItem {

    private String productId;
    private String productName;
    private BigDecimal price;

    public WishlistItem(String productId, String productName, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public WishlistItem() {
    }
}
