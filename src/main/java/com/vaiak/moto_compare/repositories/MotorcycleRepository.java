package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long>, JpaSpecificationExecutor<Motorcycle> {

    List<Motorcycle> findByManufacturer(String manufacturer);

    @NonNull
    Optional<Motorcycle> findById(@NonNull Long motorcycleId);

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

    @Query(value = """
            SELECT m.* FROM motorcycles m 
            WHERE m.category = CAST(:category AS TEXT) 
            OR ABS(m.displacement - :displacement) <= (:displacement * 0.3)
            """, nativeQuery = true)
    List<Motorcycle> findCandidateSimilarMotorcycles(
            @Param("category") Category category,
            @Param("displacement") int displacement
    );
}
