package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSearchParams;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.dto.motorcycle.RankedMotorcycleDTO;
import com.vaiak.moto_compare.enums.Manufacturer;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.UserFavorite;
import com.vaiak.moto_compare.services.MotorcycleService;
import com.vaiak.moto_compare.services.UserFavoriteService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/motorcycles")
public class MotorcycleController {

  private final MotorcycleService motorcycleService;
  private final UserFavoriteService userFavoriteService;
  private final MeterRegistry meterRegistry;

  public MotorcycleController(MotorcycleService motorcycleService,
                              UserFavoriteService userFavoriteService,
                              MeterRegistry meterRegistry) {
    this.motorcycleService = motorcycleService;
    this.userFavoriteService = userFavoriteService;
    this.meterRegistry = meterRegistry;
  }

  @GetMapping
  public ResponseEntity<PagedModel<MotorcycleSummaryDTO>> getAllSummary(
          @ModelAttribute MotorcycleSearchParams params) {
    String mode = resolveDiscoveryMode(params);
    meterRegistry.counter("moto_discovery_requests_total", "mode", mode).increment();

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

    Page<MotorcycleSummaryDTO> allMotorcyclesSummary = motorcycleService.getAllMotorcycles(params);

    PagedModel<MotorcycleSummaryDTO> pagedModel = new PagedModel<>(allMotorcyclesSummary);
    PagedModel.PageMetadata metadata = Objects.requireNonNull(pagedModel.getMetadata());

    return ResponseEntity.ok()
            .header("X-Total-Pages", String.valueOf(metadata.totalPages()))
            .header("X-Total-Elements", String.valueOf(metadata.totalElements()))
            .header("Access-Control-Expose-Headers", "X-Total-Pages, X-Total-Elements")
            .body(pagedModel);
  }

  @GetMapping("/{motorcycleId}")
  public ResponseEntity<MotorcycleDetailsDTO> getMotorcycleDetails(@PathVariable Long motorcycleId) {
    MotorcycleDetailsDTO motorcycleDetailsById = motorcycleService.getMotorcycleDetailsById(motorcycleId);

    meterRegistry.counter(
        "moto_motorcycle_details_views_total",
        "manufacturer", motorcycleDetailsById.getManufacturer().name(),
        "category", motorcycleDetailsById.getCategory().name())
        .increment();
    meterRegistry.counter(
        "moto_interest_manufacturer_total",
        "manufacturer", motorcycleDetailsById.getManufacturer().name(),
        "source", "details")
        .increment();
    meterRegistry.counter(
        "moto_interest_category_total",
        "category", motorcycleDetailsById.getCategory().name(),
        "source", "details")
        .increment();

    return ResponseEntity.ok(motorcycleDetailsById);
  }

  @GetMapping("/{motorcycleId}/summary")
  public ResponseEntity<MotorcycleSummaryDTO> getMotorcycleSummary(@PathVariable Long motorcycleId) {
    MotorcycleSummaryDTO motorcycleSummaryById = motorcycleService.getMotorcycleSummaryById(motorcycleId);
    return ResponseEntity.ok(motorcycleSummaryById);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<MotorcycleDetailsDTO> createMotorcycle(@Valid @RequestBody MotorcycleDetailsDTO motorcycle) {
    motorcycleService.saveMotorcycle(motorcycle);
    return ResponseEntity.status(HttpStatus.CREATED).body(motorcycle);
  }

  @GetMapping("/brand/{manufacturer}")
  public ResponseEntity<List<Motorcycle>> getByManufacturer(@PathVariable String manufacturer) {
    List<Motorcycle> motorcyclesByManufacturer = motorcycleService.getMotorcyclesByManufacturer(manufacturer);
    return ResponseEntity.ok(motorcyclesByManufacturer);
  }

  @GetMapping("/favorites")
  @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
  public ResponseEntity<List<MotorcycleSummaryDTO>> getFavoriteMotorcyclesByUser(Authentication auth) {
    return ResponseEntity.ok(userFavoriteService.getFavoriteMotorcyclesByUser(auth));
  }

  @PostMapping("/{motorcycleId}/favorites")
  @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
  public ResponseEntity<UserFavorite> addMotorcycleToFavorites(@PathVariable Long motorcycleId,
                                                               Authentication auth) {
    return ResponseEntity.ok(userFavoriteService.saveFavoriteMotorcycle(motorcycleId, auth));
  }

  @DeleteMapping("/{motorcycleId}/favorites")
  @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
  public ResponseEntity<String> removeMotorcycleFromFavorites(@PathVariable Long motorcycleId,
                                                              Authentication auth) {
    userFavoriteService.removeFavoriteMotorcycle(motorcycleId, auth);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(motorcycleId + " removed from favorites");
  }

  @PostMapping("/quiz")
  public ResponseEntity<List<RankedMotorcycleDTO>> getQuizMotorcyclesResult(@RequestBody QuizAnswersDTO quizAnswersDTO) {
    try {
      List<RankedMotorcycleDTO> result = motorcycleService.getMotorcyclesBasedOnQuizAnswers(quizAnswersDTO);
      meterRegistry.counter("app_quiz_requests_total", "result", "success").increment();

      List<RankedMotorcycleDTO> topResult = result.subList(0, Math.min(result.size(), 5));
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

      return ResponseEntity.ok(topResult);
    } catch (RuntimeException ex) {
      meterRegistry.counter("app_quiz_requests_total", "result", "failure").increment();
      throw ex;
    }
  }

  private String resolveDiscoveryMode(MotorcycleSearchParams params) {
    if (params.getSearch() != null && !params.getSearch().isBlank()) {
      return "search";
    }

    boolean hasFilter = params.getCategory() != null
        || (params.getManufacturer() != null && !params.getManufacturer().isBlank())
        || params.getHorsePowerMin() != null
        || params.getHorsePowerMax() != null
        || params.getDisplacementMin() != null
        || params.getDisplacementMax() != null
        || params.getYearMin() != null
        || params.getYearMax() != null;

    return hasFilter ? "filter" : "browse";
  }

  private String normalizeManufacturer(String manufacturer) {
    try {
      return Manufacturer.valueOf(manufacturer.trim().toUpperCase(Locale.ROOT)).name();
    } catch (IllegalArgumentException ex) {
      return "OTHER";
    }
  }
}
