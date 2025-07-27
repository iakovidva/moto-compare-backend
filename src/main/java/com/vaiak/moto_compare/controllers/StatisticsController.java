package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.statistics.CategoryStatisticsDTO;
import com.vaiak.moto_compare.dto.statistics.ManufacturerStatisticsDTO;
import com.vaiak.moto_compare.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryStatisticsDTO>> getAllCategoryStatistics() {
        return ResponseEntity.ok(statisticsService.getAllCategoriesStats());
    }

    @GetMapping("/manufacturers")
    public ResponseEntity<List<ManufacturerStatisticsDTO>> getAllManufacturersStatistics() {
        return ResponseEntity.ok(statisticsService.getAllManufacturerStatistics());
    }

}
