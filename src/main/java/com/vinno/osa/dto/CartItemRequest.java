package com.vinno.osa.dto;

import com.vinno.osa.entity.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class CartItemRequest {

    private String userId;
    private String productId;
    private int quantity;
    private List<CartItemRequest> items;
}
