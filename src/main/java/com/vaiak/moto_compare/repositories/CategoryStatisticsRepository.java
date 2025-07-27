package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.models.CategoryStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryStatisticsRepository extends JpaRepository<CategoryStatistics, Long> {

    Optional<CategoryStatistics> findByCategory(Category category);
}
