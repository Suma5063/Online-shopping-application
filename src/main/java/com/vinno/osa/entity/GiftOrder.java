package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "GiftOrder")
public class GiftOrder {

    @Id
    private String id;
    private String userId; // Optional for guest users
    private String recipientName;
    private String recipientEmail;
    private Address recipientAddress;
    private String giftMessage;
    private boolean hidePrice;
    private boolean giftWrap;
    private BigDecimal giftWrapCharge;
    private BigDecimal totalPrice;
    private List<OrderItem> items;
    private Date createdAt;
    private Date updatedAt;

    public GiftOrder() {
    }

    public GiftOrder(String id, String userId, String recipientName,
                     String recipientEmail, Address recipientAddress, String giftMessage, boolean hidePrice,
                     boolean giftWrap, BigDecimal giftWrapCharge, BigDecimal totalPrice,
                     List<OrderItem> items, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
        this.recipientAddress = recipientAddress;
        this.giftMessage = giftMessage;
        this.hidePrice = hidePrice;
        this.giftWrap = giftWrap;
        this.giftWrapCharge = giftWrapCharge;
        this.totalPrice = totalPrice;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters & Setters
}
