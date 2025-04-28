package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.manufacturer.PopularManufacturerDTO;
import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleDetailsDTO;
import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.mappers.SubmitRequestMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.services.MotorcycleService;
import com.vaiak.moto_compare.services.PopularManufacturerService;
import com.vaiak.moto_compare.services.UserRequestService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/motorcycles")
@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from your frontend
public class MotorcycleController {

  private final MotorcycleService motorcycleService;
  private final UserRequestService userRequestService;
  private final PopularManufacturerService popularManufacturerService;

  public MotorcycleController(MotorcycleService motorcycleService,
                              UserRequestService userRequestService,
                              PopularManufacturerService popularManufacturerService) {
    this.motorcycleService = motorcycleService;
    this.userRequestService = userRequestService;
    this.popularManufacturerService = popularManufacturerService;
  }

  @GetMapping
  public ResponseEntity<PagedModel<MotorcycleSummaryDTO>> getAllSummary(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "3") int size,
          @RequestParam(name = "category", required = false) Category category,
          @RequestParam(name = "manufacturer", required = false) String manufacturer,
          @RequestParam(name = "horsePowerMin", required = false) Integer horsePowerMin,
          @RequestParam(name = "horsePowerMax", required = false) Integer horsePowerMax,
          @RequestParam(name = "displacementMin", required = false) Integer displacementMin,
          @RequestParam(name = "displacementMax", required = false) Integer displacementMax,
          @RequestParam(name = "yearMin", required = false) Integer yearMin,
          @RequestParam(name = "yearMax", required = false) Integer yearMax,
          @RequestParam(name = "search", required = false) String search,
          @RequestParam(name = "sort", required = false) String sort
  ) {

    Page<MotorcycleSummaryDTO> allMotorcyclesSummary;

    if (search != null) {
      allMotorcyclesSummary = motorcycleService.searchMotos(page, size, search);
    } else {
      allMotorcyclesSummary = motorcycleService.getAllMotorcyclesSummary(page,
                    size,
                    manufacturer,
                    category,
                    horsePowerMin,
                    horsePowerMax,
                    displacementMin,
                    displacementMax,
                    yearMin,
                    yearMax,
                    sort);
    }

    PagedModel<MotorcycleSummaryDTO> pagedModel = new PagedModel<>(allMotorcyclesSummary);
    PagedModel.PageMetadata metadata = Objects.requireNonNull(pagedModel.getMetadata());

    return ResponseEntity.ok()
            .header("X-Total-Pages", String.valueOf(metadata.totalPages())) // Add metadata in headers
            .header("X-Total-Elements", String.valueOf(metadata.totalElements()))
            .header("Access-Control-Expose-Headers", "X-Total-Pages, X-Total-Elements") // 👈 Add this
            .body(pagedModel);
  }

  //TODO: RETURN ResponseEntity<> in all requests! Best practice. Example:
//  @GetMapping("/{motorcycleId}")
//  public ResponseEntity<?> getMotorcycle(@PathVariable Long motorcycleId) {
//    try {
//      MotorcycleDetailsDTO motorcycle = motorcycleService.getMotorcycleById(motorcycleId);
//      if (motorcycle == null) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ErrorResponse("Motorcycle not found with ID: " + motorcycleId));
//      }
//      return ResponseEntity.ok(motorcycle); // 200 OK with the DTO
//    } catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//              .body(new ErrorResponse("An error occurred while fetching the motorcycle."));
//    }
//  }
  @GetMapping("/{motorcycleId}")
  public MotorcycleDetailsDTO getMotorcycle(@PathVariable Long motorcycleId) {
    Motorcycle motorcycle = motorcycleService.getMotorcycleById(motorcycleId);
    MotorcycleDetailsDTO motoDetails = MotorcycleMapper.toDetailsDTO(motorcycle);
    System.out.println(motoDetails);
    return motoDetails;
  }

  @GetMapping("/{motorcycleId}/summary")
  public MotorcycleSummaryDTO getMotorcycleSummary(@PathVariable Long motorcycleId) {
    Motorcycle motorcycle = motorcycleService.getMotorcycleById(motorcycleId);
    MotorcycleSummaryDTO motoSummary = MotorcycleMapper.toSummaryDTO(motorcycle);
    System.out.println(motoSummary);
    return motoSummary;
  }

  @PostMapping
  public MotorcycleDetailsDTO createMotorcycle(@Valid @RequestBody MotorcycleDetailsDTO motorcycle) {
    Motorcycle moto = motorcycleService.saveMotorcycle(motorcycle);
    popularManufacturerService.evictPopularManufacturerCache();
    System.out.println("Moto saved: " + moto + " and similar are: " );
    moto.getSimilarMotorcycles().forEach(m -> System.out.println(m.getModel()));
    return motorcycle;
  }

  @GetMapping("/brand/{manufacturer}")
  public List<Motorcycle> getByManufacturer(@PathVariable String manufacturer) {
    return motorcycleService.getMotorcyclesByManufacturer(manufacturer);
  }

  @PostMapping("/requests")
  public ResponseEntity<?> submitMotorcycleRequest(@Valid @RequestBody SubmitMotorcycleRequestDTO motorcycleRequest,
                                                   BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      List<String> errors = bindingResult.getAllErrors().stream()
              .map(error -> error.getObjectName() + " : " + error.getDefaultMessage())
              .toList();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    UserRequest userRequest = SubmitRequestMapper.toUserRequest(motorcycleRequest);
    UserRequest savedRequest = userRequestService.createRequest(userRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
  }

  @PostMapping("/{motorcycleId}/incorrectValue")
  public ResponseEntity<?> submitIncorrectValueReport(@PathVariable String motorcycleId,
                                                      @RequestBody IncorrectSpecReportDTO requestContent) {
    System.out.println("=====================".repeat(3));
    System.out.println(requestContent);
    System.out.println(motorcycleId);
    UserRequest userRequest = SubmitRequestMapper.toUserRequest(requestContent);
    UserRequest savedRequest = userRequestService.createRequest(userRequest);
    System.out.println("=====================".repeat(3));
    return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
  }

  @GetMapping("/requests")
  public List<UserRequest> getAllRequests() {
    return userRequestService.getAllRequests();
  }

  @GetMapping("/popular-manufacturers")
  public List<PopularManufacturerDTO> getPopularManufacturers() {
    return popularManufacturerService.getPopularManufacturers();
  }
}
