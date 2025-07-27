package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSearchParams;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.dto.motorcycle.RankedMotorcycleDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.UserFavorite;
import com.vaiak.moto_compare.services.MotorcycleService;
import com.vaiak.moto_compare.services.UserFavoriteService;
import jakarta.validation.Valid;
import java.util.List;
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

  public MotorcycleController(MotorcycleService motorcycleService,
                              UserFavoriteService userFavoriteService) {
    this.motorcycleService = motorcycleService;
    this.userFavoriteService = userFavoriteService;
  }

  @GetMapping
  public ResponseEntity<PagedModel<MotorcycleSummaryDTO>> getAllSummary(
          @ModelAttribute MotorcycleSearchParams params) {

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
    List<RankedMotorcycleDTO> result = motorcycleService.getMotorcyclesBasedOnQuizAnswers(quizAnswersDTO);
    return ResponseEntity.ok(result.subList(0, Math.min(result.size(), 5)));
  }
}
