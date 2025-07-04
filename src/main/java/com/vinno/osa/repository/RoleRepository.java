package com.vinno.osa.repository;

import com.vinno.osa.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role,String> {

    Optional<Role> findByRoleName(String roleName);
}
