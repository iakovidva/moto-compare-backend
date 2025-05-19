package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    User findByUserName(String username);

}
