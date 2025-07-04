package com.vinno.osa.repository;

import com.vinno.osa.entity.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CouponRepository extends MongoRepository<Coupon,String> {

    Optional<Coupon> findByCode(String couponCode);


}
