package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.models.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {

    @Query("SELECT ur FROM UserRequest ur WHERE ur.user.id = :userId")
    List<UserRequest> getRequestsForUser(@Param("userId") UUID userId);

}
