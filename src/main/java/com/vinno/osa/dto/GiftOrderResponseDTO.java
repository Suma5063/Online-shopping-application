package com.vinno.osa.dto;

import com.vinno.osa.entity.GiftOrder;
import com.vinno.osa.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GiftOrderResponseDTO {

    private String id;
    private String userId;
    private String recipientName;
    private String recipientEmail;
    private String recipientAddress;
    private String giftMessage;
    private BigDecimal totalPrice;
    private List<OrderItem> items;
    private Date createdAt;
    private Date updatedAt;

    public GiftOrderResponseDTO() {
    }
}
