package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.statistics.CategoryStatisticsDTO;
import com.vaiak.moto_compare.dto.statistics.ManufacturerStatisticsDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import com.vaiak.moto_compare.models.CategoryStatistics;
import com.vaiak.moto_compare.models.ManufacturerStatistics;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.repositories.CategoryStatisticsRepository;
import com.vaiak.moto_compare.repositories.ManufacturersStatisticsRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private final CategoryStatisticsRepository categoryStatisticsRepository;
    private final ManufacturersStatisticsRepository manufacturersStatisticsRepository;

    public StatisticsService(CategoryStatisticsRepository categoryStatisticsRepository,
                             ManufacturersStatisticsRepository manufacturersStatisticsRepository) {
        this.categoryStatisticsRepository = categoryStatisticsRepository;
        this.manufacturersStatisticsRepository = manufacturersStatisticsRepository;
    }

    @Cacheable("categoryStats")
    public List<CategoryStatisticsDTO> getAllCategoriesStats() {
        return categoryStatisticsRepository.findAll()
                .stream()
                .map(category -> CategoryStatisticsDTO.builder()
                        .category(category.getCategory())
                        .counter(category.getCounter())
                        .build())
                .toList();
    }

    @Cacheable("manufacturerStats")
    public List<ManufacturerStatisticsDTO> getAllManufacturerStatistics() {
        return manufacturersStatisticsRepository.findAll()
                .stream()
                .map(ms -> ManufacturerStatisticsDTO.builder()
                        .manufacturer(ms.getManufacturer())
                        .counter(ms.getCounter())
                        .build())
                .toList();
    }

    @CacheEvict(cacheNames = {"categoryStats", "manufacturerStats"}, allEntries = true)
    public void evictStatisticsCache() {
        System.out.println("statistics cache evicted.");
    }

    @Transactional
    public void updateStatistics(Motorcycle moto) {
        Manufacturer manufacturer = moto.getManufacturer();
        Category category = moto.getCategory();
        updateManufacturerStatistics(manufacturer);
        updateCategoryStatistics(category);
    }

    private void updateManufacturerStatistics(Manufacturer manufacturer) {
        manufacturersStatisticsRepository
                .findByManufacturer(manufacturer)
                .ifPresentOrElse( stat -> {
                            stat.setCounter(stat.getCounter() + 1);
                            manufacturersStatisticsRepository.save(stat);
                        },
                        () -> manufacturersStatisticsRepository.save(ManufacturerStatistics.builder()
                                .manufacturer(manufacturer)
                                .counter(1L)
                                .build()));
    }


    private void updateCategoryStatistics(Category category) {
    categoryStatisticsRepository
        .findByCategory(category)
        .ifPresentOrElse(
            stat -> {
              stat.setCounter(stat.getCounter() + 1);
              categoryStatisticsRepository.save(stat);
            },
            () -> categoryStatisticsRepository.save(CategoryStatistics.builder()
                    .category(category)
                    .counter(1L)
                    .build()));
    }
}
