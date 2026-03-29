package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSearchParams;
import com.vaiak.moto_compare.dto.motorcycle.RankedMotorcycleDTO;
import com.vaiak.moto_compare.enums.Manufacturer;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class MeterRegistryService {

    private final MeterRegistry meterRegistry;

    public MeterRegistryService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void countGetAllRequests(MotorcycleSearchParams params) {
        if (params.getManufacturer() != null && !params.getManufacturer().isBlank()) {
            meterRegistry.counter(
                            "moto_interest_manufacturer_total",
                            "manufacturer", normalizeManufacturer(params.getManufacturer()),
                            "source", "list")
                    .increment();
        }

        if (params.getCategory() != null) {
            meterRegistry.counter(
                            "moto_interest_category_total",
                            "category", params.getCategory().name(),
                            "source", "list")
                    .increment();
        }
    }

    private String normalizeManufacturer(String manufacturer) {
        try {
            return Manufacturer.valueOf(manufacturer.trim().toUpperCase(Locale.ROOT)).name();
        } catch (IllegalArgumentException ex) {
            return "OTHER";
        }
    }

    public void countGetMotorcycleDetailsRequests(MotorcycleDetailsDTO moto) {
        meterRegistry.counter(
                        "moto_motorcycle_details_views_total",
                        "manufacturer", moto.getManufacturer().name(),
                        "category", moto.getCategory().name())
                .increment();
        meterRegistry.counter(
                        "moto_interest_manufacturer_total",
                        "manufacturer", moto.getManufacturer().name(),
                        "source", "details")
                .increment();
        meterRegistry.counter(
                        "moto_interest_category_total",
                        "category", moto.getCategory().name(),
                        "source", "details")
                .increment();
    }

    public void countQuizResults(List<RankedMotorcycleDTO> topResult) {
        meterRegistry.counter("app_quiz_requests_total", "result", "success").increment();

        topResult.forEach(moto -> {
            meterRegistry.counter(
                            "moto_interest_manufacturer_total",
                            "manufacturer", moto.getManufacturer().name(),
                            "source", "quiz_result")
                    .increment();
            meterRegistry.counter(
                            "moto_interest_category_total",
                            "category", moto.getCategory().name(),
                            "source", "quiz_result")
                    .increment();
        });
    }
}
