package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSearchParams;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.exceptions.MotorcycleNotFoundException;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.repositories.MotorcycleRepository;
import com.vaiak.moto_compare.repositories.specifications.MotoSpecs;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotorcycleService {

  private final MotorcycleRepository repository;
  private final PopularManufacturerService popularManufacturerService;

  public MotorcycleService(MotorcycleRepository repository,
                           PopularManufacturerService popularManufacturerService) {
    this.repository = repository;
    this.popularManufacturerService = popularManufacturerService;
  }

  public Page<MotorcycleSummaryDTO> getAllMotorcycles(MotorcycleSearchParams params) {
    if (params.getSearch() != null) {
      return searchMotos(params.getPage(), params.getSize(), params.getSearch());
    } else {
      return getFilteredMotorcycles(params);
    }
  }

  public Page<MotorcycleSummaryDTO> getFilteredMotorcycles(MotorcycleSearchParams params) {
    Sort sortBy = parseSorting(params.getSort());

    Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), sortBy);
    // Consider switching to manual Queries if that starts taking so long. (refactor with between?)
    Specification<Motorcycle> spec = Specification.where(
            (params.getManufacturer() != null && !params.getManufacturer().isEmpty()) ? MotoSpecs.hasManufacturer(params.getManufacturer()) : null)
            .and((params.getCategory() != null ? MotoSpecs.isCategory(params.getCategory()) : null))
            .and(params.getHorsePowerMin() != null ? MotoSpecs.hasHorsePowerMin(params.getHorsePowerMin()) : null)
            .and(params.getHorsePowerMax() != null ? MotoSpecs.hasHorsePowerMax(params.getHorsePowerMax()) : null)
            .and(params.getDisplacementMin() != null ? MotoSpecs.hasDisplacementMin(params.getDisplacementMin()) : null)
            .and(params.getDisplacementMax() != null ? MotoSpecs.hasDisplacementMax(params.getDisplacementMax()) : null)
            .and(params.getYearMin() != null ? MotoSpecs.hasYearMin(params.getYearMin()) : null)
            .and(params.getYearMax() != null ? MotoSpecs.hasYearMax(params.getYearMax()) : null);
    Page<Motorcycle> summaryMotorcycles = repository.findAll(spec, pageable);

    return summaryMotorcycles.map(MotorcycleMapper::toSummaryDTO);
  }

  private Sort parseSorting(String sort) {
    if (sort == null || sort.isEmpty()) {
      return Sort.unsorted();
    }

    return switch (sort) {
      case "latest" -> Sort.by("updatedAt").descending();
      case "horsePower_desc" -> Sort.by("horsePower").descending();
      case "horsePower_asc" -> Sort.by("horsePower").ascending();
      case "displacement_desc" -> Sort.by("displacement").descending();
      case "displacement_asc" -> Sort.by("displacement").ascending();
      case "date_desc" -> Sort.by("yearRange").descending();
      case "date_asc" -> Sort.by("yearRange").ascending();
      default -> Sort.unsorted();
    };
  }

  @Transactional
  public void saveMotorcycle(MotorcycleDetailsDTO motorcycleDTO) {
    Motorcycle moto = MotorcycleMapper.toEntity(motorcycleDTO);
    moto.setSimilarMotorcycles(repository.findSimilarMotorcycles(moto.getCategory(), moto.getHorsePower(), moto.getDisplacement()));
    repository.save(moto);
    popularManufacturerService.evictPopularManufacturerCache();
  }

  public List<Motorcycle> getMotorcyclesByManufacturer(String manufacturer) {
    List<Motorcycle> byManufacturer = repository.findByManufacturer(manufacturer);
    if (byManufacturer.isEmpty()) {
      throw new MotorcycleNotFoundException(manufacturer);
    }
    return byManufacturer;
  }

  public MotorcycleDetailsDTO getMotorcycleDetailsById(Long motorcycleId) {
    Motorcycle motorcycle = repository.findById(motorcycleId)
            .orElseThrow(() -> new MotorcycleNotFoundException(motorcycleId));
    return MotorcycleMapper.toDetailsDTO(motorcycle);
  }

  public MotorcycleSummaryDTO getMotorcycleSummaryById(Long motorcycleId) {
    Motorcycle motorcycle = repository.findById(motorcycleId)
            .orElseThrow(() -> new MotorcycleNotFoundException(motorcycleId));
    return MotorcycleMapper.toSummaryDTO(motorcycle);
  }

  public Page<MotorcycleSummaryDTO> searchMotos(int page, int size, String keyword) {
    Pageable pageable = PageRequest.of(page, size);
    return repository.searchMotorcycles(keyword, pageable);
  }

  public Motorcycle getMotorcycleById(Long motorcycleId) {
    return repository.findById(motorcycleId).orElseThrow();
  }
}
