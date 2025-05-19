package com.vaiak.moto_compare.security.refreshToken;

import com.vaiak.moto_compare.security.models.RefreshToken;
import com.vaiak.moto_compare.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
