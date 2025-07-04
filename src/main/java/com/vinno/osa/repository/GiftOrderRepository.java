package com.vinno.osa.repository;

import com.vinno.osa.entity.GiftOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftOrderRepository extends MongoRepository<GiftOrder,String> {

    List<GiftOrder> findByUserId(String userId);

}
