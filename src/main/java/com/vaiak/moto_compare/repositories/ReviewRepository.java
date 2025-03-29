package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMotorcycleId(Long motorcycleId);
}
