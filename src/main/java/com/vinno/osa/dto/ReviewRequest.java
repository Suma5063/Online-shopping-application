package com.vinno.osa.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private String productId;
    private String userId;
    private int rating;
    private String comment;
}
