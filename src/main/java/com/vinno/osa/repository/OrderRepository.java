package com.vinno.osa.repository;

import com.vinno.osa.entity.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Orders,String> {

    List<Orders> findByUserId(String userId);

    Orders getOrderById(String orderId);

    List<Orders> getOrdersByUserId(String userId);

    List<Orders> findByEmail(String userEmail);
}
