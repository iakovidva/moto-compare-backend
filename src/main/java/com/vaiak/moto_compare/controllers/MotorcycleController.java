package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.manufacturer.PopularManufacturerDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSearchParams;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.services.MotorcycleService;
import com.vaiak.moto_compare.services.PopularManufacturerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/motorcycles")
@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from your frontend
public class MotorcycleController {

  private final MotorcycleService motorcycleService;
  private final PopularManufacturerService popularManufacturerService;

  public MotorcycleController(MotorcycleService motorcycleService,
                              PopularManufacturerService popularManufacturerService) {
    this.motorcycleService = motorcycleService;
    this.popularManufacturerService = popularManufacturerService;
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
  public ResponseEntity<MotorcycleDetailsDTO> createMotorcycle(@Valid @RequestBody MotorcycleDetailsDTO motorcycle) {
    motorcycleService.saveMotorcycle(motorcycle);
    return ResponseEntity.status(HttpStatus.CREATED).body(motorcycle);
  }

  @GetMapping("/brand/{manufacturer}")
  public ResponseEntity<List<Motorcycle>> getByManufacturer(@PathVariable String manufacturer) {
    List<Motorcycle> motorcyclesByManufacturer = motorcycleService.getMotorcyclesByManufacturer(manufacturer);
    return ResponseEntity.ok(motorcyclesByManufacturer);
  }

  @GetMapping("/popular-manufacturers")
  public List<PopularManufacturerDTO> getPopularManufacturers() {
    return popularManufacturerService.getPopularManufacturers();
  }
}
