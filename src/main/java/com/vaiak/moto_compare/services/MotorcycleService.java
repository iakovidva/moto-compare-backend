package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.repositories.MotorcycleRepository;
import com.vaiak.moto_compare.repositories.specifications.MotoSpecs;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotorcycleService {

  private final MotorcycleRepository repository;

  public MotorcycleService(MotorcycleRepository repository) {
    this.repository = repository;
  }

  public Page<MotorcycleSummaryDTO> getAllMotorcyclesSummary(int page, int size, String manufacturer) {
    Pageable pageable = PageRequest.of(page, size);
    // Consider switching to manual Queries if that starts taking so long.
    Specification<Motorcycle> spec = Specification.where(
            (manufacturer != null && !manufacturer.isEmpty()) ? MotoSpecs.hasManufacturer(manufacturer) : null);
    Page<Motorcycle> summaryMotorcycles = repository.findAll(spec, pageable);

    return summaryMotorcycles.map(MotorcycleMapper::toSummaryDTO);
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
}
