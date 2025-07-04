package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Document(collection = "Reviews")
public class Review {

    private String userId;
    private String productId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private Date date;

    public Review() {
    }



}
