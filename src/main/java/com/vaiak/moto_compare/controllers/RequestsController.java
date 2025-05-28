package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.services.UserRequestService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/motorcycles")
public class RequestsController {

    private final UserRequestService userRequestService;

    public RequestsController(UserRequestService userRequestService) {
        this.userRequestService = userRequestService;
    }

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        List<UserRequest> allRequests = userRequestService.getAllRequests();
        return ResponseEntity.ok(allRequests);
    }

    @PostMapping("/requests")
    public ResponseEntity<String> submitMotorcycleRequest(@Valid @RequestBody SubmitMotorcycleRequestDTO motorcycleRequest,
                                                          @RequestHeader(value = "Authorization", required = false) String authHeader) {

        UserRequest request = userRequestService.createNewMotorcycleRequest(motorcycleRequest, authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(request.getRequestContent());
    }

    @PostMapping("/{motorcycleId}/incorrectValue")
    public ResponseEntity<String> submitIncorrectValueReport(@PathVariable String motorcycleId,
                                                             @RequestBody IncorrectSpecReportDTO requestContent,
                                                             @RequestHeader(value = "Authorization", required = false) String authHeader) {
        UserRequest request = userRequestService.createIncorrectValueRequest(requestContent, authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(request.getRequestContent());
    }
}
