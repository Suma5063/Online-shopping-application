package com.vinno.osa.repository;

import com.vinno.osa.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review,String> {

    List<Review> findByProductId(String productId);
    List<Review> findByUserId(String userId);
}
