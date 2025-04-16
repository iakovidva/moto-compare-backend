package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.enums.Category;
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

  public MotorcycleService(MotorcycleRepository repository) {
    this.repository = repository;
  }

  public Page<MotorcycleSummaryDTO> getAllMotorcyclesSummary(int page,
                                                             int size,
                                                             String manufacturer,
                                                             Category category,
                                                             Integer horsePowerMin,
                                                             Integer horsePowerMax,
                                                             Integer displacementMin,
                                                             Integer displacementMax,
                                                             Integer yearMin,
                                                             Integer yearMax,
                                                             String sort
  ) {
    Sort sortBy = parseSorting(sort);

    Pageable pageable = PageRequest.of(page, size, sortBy);
    // Consider switching to manual Queries if that starts taking so long. (refactor with between?)
    Specification<Motorcycle> spec = Specification.where(
            (manufacturer != null && !manufacturer.isEmpty()) ? MotoSpecs.hasManufacturer(manufacturer) : null)
            .and((category != null ? MotoSpecs.isCategory(category) : null))
            .and(horsePowerMin != null ? MotoSpecs.hasHorsePowerMin(horsePowerMin) : null)
            .and(horsePowerMax != null ? MotoSpecs.hasHorsePowerMax(horsePowerMax) : null)
            .and(displacementMin != null ? MotoSpecs.hasDisplacementMin(displacementMin) : null)
            .and(displacementMax != null ? MotoSpecs.hasDisplacementMax(displacementMax) : null)
            .and(yearMin != null ? MotoSpecs.hasYearMin(yearMin) : null)
            .and(yearMax != null ? MotoSpecs.hasYearMax(yearMax) : null);
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
      default -> Sort.unsorted(); // fallback
    };
  }

  @Transactional
  public Motorcycle saveMotorcycle(MotorcycleDetailsDTO motorcycleDTO) {
    Motorcycle moto = MotorcycleMapper.toEntity(motorcycleDTO);
    moto.setSimilarMotorcycles(repository.findSimilarMotorcycles(moto.getCategory(), moto.getHorsePower(), moto.getDisplacement()));
    repository.save(moto);
    return moto;
  }

  public List<Motorcycle> getMotorcyclesByManufacturer(String manufacturer) {
    return repository.findByManufacturer(manufacturer);
  }

  public Motorcycle getMotorcycleById(Long motorcycleId) {
    return repository.findById(motorcycleId).orElseThrow();
  }

  public Page<MotorcycleSummaryDTO> searchMotos(int page, int size, String keyword) {
    Pageable pageable = PageRequest.of(page, size);
    return repository.searchMotorcycles(keyword, pageable);
  }

}
