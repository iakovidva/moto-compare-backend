package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.manufacturer.PopularManufacturerDTO;
import com.vaiak.moto_compare.repositories.MotorcycleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularManufacturerService {

    private final MotorcycleRepository motorcycleRepository;

    public PopularManufacturerService(MotorcycleRepository motorcycleRepository) {
        this.motorcycleRepository = motorcycleRepository;
    }

    @Cacheable("popularManufacturers")
    public List<PopularManufacturerDTO> getPopularManufacturers() {
        return motorcycleRepository.getPopularManufacturers();
    }

    @CacheEvict(value = "popularManufacturers", allEntries = true)
    public void evictPopularManufacturerCache() {
        System.out.println("Popular manufacturer cache evicted.");
    }
}