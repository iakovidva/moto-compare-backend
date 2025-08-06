package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.dto.statistics.CategoryStatisticsDTO;
import com.vaiak.moto_compare.dto.statistics.ManufacturerStatisticsDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.models.CategoryStatistics;
import com.vaiak.moto_compare.models.ManufacturerStatistics;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.repositories.CategoryStatisticsRepository;
import com.vaiak.moto_compare.repositories.ManufacturersStatisticsRepository;
import com.vaiak.moto_compare.repositories.MotorcycleRepository;
import com.vaiak.moto_compare.repositories.ReviewRepository;
import com.vaiak.moto_compare.repositories.UserFavoriteRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StatisticsService {

    private final CategoryStatisticsRepository categoryStatisticsRepository;
    private final ManufacturersStatisticsRepository manufacturersStatisticsRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final ReviewRepository reviewRepository;
    private final MotorcycleRepository motorcycleRepository;

    public StatisticsService(CategoryStatisticsRepository categoryStatisticsRepository,
                             ManufacturersStatisticsRepository manufacturersStatisticsRepository,
                             UserFavoriteRepository userFavoriteRepository,
                             ReviewRepository reviewRepository,
                             MotorcycleRepository motorcycleRepository) {
        this.categoryStatisticsRepository = categoryStatisticsRepository;
        this.manufacturersStatisticsRepository = manufacturersStatisticsRepository;
        this.userFavoriteRepository = userFavoriteRepository;
        this.reviewRepository = reviewRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    @Cacheable("popular")
    public List<MotorcycleSummaryDTO> getPopularMotorcycles() {
        Map<Long, Double> motorcycleIdWithPopularity = new HashMap<>();
        List<Object[]> motorcycleFavoriteCounts = userFavoriteRepository.getMotorcycleFavoriteCounts();
        motorcycleFavoriteCounts.forEach(row -> motorcycleIdWithPopularity.put((Long) row[0], (Long) row[1] * 1.5));

        List<Object[]> motorcycleReviewCounts = reviewRepository.getMotorcycleReviewCounts();
        motorcycleReviewCounts.forEach(row -> {
            Long key = (Long) row[0];
            if (motorcycleIdWithPopularity.containsKey(key)) {
                motorcycleIdWithPopularity.put(key, motorcycleIdWithPopularity.get(key) + (Long) row[1]);
            } else {
                motorcycleIdWithPopularity.put(key, (Long) row[1] * 1.0);
            }
        });

        return motorcycleIdWithPopularity.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(4)
                .map(entry -> motorcycleRepository.findById(entry.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .map(MotorcycleMapper::toSummaryDTO)
                .toList();
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
