package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMotorcycleId(Long motorcycleId);
    List<Review> findAllByUserId(UUID userId);

    @Query("""
    SELECT r.motorcycle.id, COUNT(r.motorcycle.id)
    FROM Review r
    GROUP BY r.motorcycle.id
    ORDER BY COUNT(r.motorcycle.id) DESC
    """)
    List<Object[]> getMotorcycleReviewCounts();

    Optional<Review> findByMotorcycleIdAndUserId(Long motorcycleId, UUID userId);
}
