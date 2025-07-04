package com.vinno.osa.repository;

import com.vinno.osa.entity.GuestOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GuestOrderRepository extends MongoRepository<GuestOrder,String> {

    Optional<GuestOrder> findByEmailAndId(String email, String orderId);
}
