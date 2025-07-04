package com.vinno.osa.repository;


import com.vinno.osa.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<Wishlist,String> {

    Optional<Wishlist> findByUserId(String userId);
}
