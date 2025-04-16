package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.dto.manufacturer.PopularManufacturerDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long>, JpaSpecificationExecutor<Motorcycle> {

    List<Motorcycle> findByManufacturer(String manufacturer);

    Optional<Motorcycle> findById(Long motorcycleId);

    @Query("SELECT new com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO(m.id, m.manufacturer, m.model, m.yearRange, m.image, m.category, m.displacement, m.horsePower) FROM Motorcycle m")
    Page<MotorcycleSummaryDTO> findAllMotorcyclesSummary(Pageable pageable);

  @Query(
      """
    SELECT new com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO(m.id, m.manufacturer, m.model, m.yearRange, m.image, m.category, m.displacement, m.horsePower)
    FROM Motorcycle m WHERE
    LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(m.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(m.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
  Page<MotorcycleSummaryDTO> searchMotorcycles(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM motorcycles m WHERE m.category = CAST(:category AS TEXT)  " +
            "AND ABS(m.displacement - :displacement) <= 350 " +
            "AND ABS(m.horse_power - :horsePower) <= 40 " +
            "LIMIT 5", nativeQuery = true)
    Set<Motorcycle> findSimilarMotorcycles(@Param("category") Category category,
                                           @Param("horsePower") int horsePower,
                                           @Param("displacement") int displacement);

    @Query(value = """
    SELECT new com.vaiak.moto_compare.dto.manufacturer.PopularManufacturerDTO(
        m.manufacturer,
        COUNT(*) as count
    )
    FROM Motorcycle m
    GROUP BY m.manufacturer
    ORDER BY COUNT(*) DESC
""")
  List<PopularManufacturerDTO> getPopularManufacturers();
}
