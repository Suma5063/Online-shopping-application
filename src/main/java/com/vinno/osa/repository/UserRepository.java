package com.vinno.osa.repository;

import com.vinno.osa.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);
   //  User findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Object> findByUsername(String username);

  @Query("{ 'role.roleName': 'BUYER' }")
  List<User> findAllBuyers();

}
