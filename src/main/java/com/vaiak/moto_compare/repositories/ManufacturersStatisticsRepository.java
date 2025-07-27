package com.vaiak.moto_compare.repositories;

import com.vaiak.moto_compare.enums.Manufacturer;
import com.vaiak.moto_compare.models.ManufacturerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturersStatisticsRepository extends JpaRepository<ManufacturerStatistics, Long> {

    Optional<ManufacturerStatistics> findByManufacturer(Manufacturer manufacturer);
}
