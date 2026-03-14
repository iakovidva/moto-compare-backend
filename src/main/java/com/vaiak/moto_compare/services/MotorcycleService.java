package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSearchParams;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.dto.motorcycle.RankedMotorcycleDTO;
import com.vaiak.moto_compare.exceptions.MotorcycleNotFoundException;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.repositories.MotorcycleRepository;
import com.vaiak.moto_compare.repositories.specifications.MotoSpecs;
import com.vaiak.moto_compare.services.quiz.ScoringService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class MotorcycleService {

  private final MotorcycleRepository repository;
  private final ScoringService scoringService;
  private final StatisticsService statisticsService;
  private final MotorcycleSimilarityService similarityService;

  public MotorcycleService(MotorcycleRepository repository,
                           ScoringService scoringService,
                           StatisticsService statisticsService,
                           MotorcycleSimilarityService similarityService) {
    this.repository = repository;
    this.scoringService = scoringService;
    this.statisticsService = statisticsService;
    this.similarityService = similarityService;
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
    List<Specification<Motorcycle>> filters = new ArrayList<>();

    if (params.getManufacturer() != null && !params.getManufacturer().isBlank()) {
      filters.add(MotoSpecs.hasManufacturer(params.getManufacturer()));
    }
    if (params.getCategory() != null) {
      filters.add(MotoSpecs.isCategory(params.getCategory()));
    }
    if (params.getHorsePowerMin() != null) {
      filters.add(MotoSpecs.hasHorsePowerMin(params.getHorsePowerMin()));
    }
    if (params.getHorsePowerMax() != null) {
      filters.add(MotoSpecs.hasHorsePowerMax(params.getHorsePowerMax()));
    }
    if (params.getDisplacementMin() != null) {
      filters.add(MotoSpecs.hasDisplacementMin(params.getDisplacementMin()));
    }
    if (params.getDisplacementMax() != null) {
      filters.add(MotoSpecs.hasDisplacementMax(params.getDisplacementMax()));
    }
    if (params.getYearMin() != null) {
      filters.add(MotoSpecs.hasYearMin(params.getYearMin()));
    }
    if (params.getYearMax() != null) {
      filters.add(MotoSpecs.hasYearMax(params.getYearMax()));
    }

    Specification<Motorcycle> spec = filters.isEmpty()
        ? Specification.unrestricted()
        : Specification.allOf(filters);

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

    // Get candidate motorcycles from repository
    List<Motorcycle> candidates = repository.findCandidateSimilarMotorcycles(
        moto.getCategory(),
        moto.getDisplacement()
    );

    // Calculate similarity using the service
      //TODO Create a service that re-calculates all similar motorcycles for all motorcycles.
      // or at least in batches (like all adventures, all sport, all...) to be up to date.
      // because similar motorcycles will of course change after a while of putting more and more motorcycles!!!
    List<Motorcycle> similarMotorcycles = similarityService.findSimilarMotorcycles(candidates, moto, 6);
    moto.setSimilarMotorcycles(Set.copyOf(similarMotorcycles));

    repository.save(moto);
    statisticsService.updateStatistics(moto);
    statisticsService.evictStatisticsCache();
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

  public List<RankedMotorcycleDTO> getMotorcyclesBasedOnQuizAnswers(QuizAnswersDTO quizAnswersDTO) {
    Specification<Motorcycle> spec = MotoSpecs.fromQuizAnswers(quizAnswersDTO);
    List<Motorcycle> motorcycles = repository.findAll(spec);

    return motorcycles.stream()
            .map(motorcycle -> rankMotorcycle(motorcycle, quizAnswersDTO))
            .sorted(Comparator.comparingDouble(RankedMotorcycleDTO::getScore).reversed()) // high score first
            .toList();
  }

  private RankedMotorcycleDTO rankMotorcycle(Motorcycle motorcycle, QuizAnswersDTO quizAnswersDTO) {
    double score = scoringService.calculateTotalScore(motorcycle, quizAnswersDTO);

    MotorcycleSummaryDTO base = MotorcycleMapper.toSummaryDTO(motorcycle);
    return RankedMotorcycleDTO.of(base, motorcycle.getWeight(), motorcycle.getFuelConsumption(), score);
  }

}
