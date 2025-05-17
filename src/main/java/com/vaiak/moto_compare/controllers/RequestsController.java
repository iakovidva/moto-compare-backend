package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.services.UserRequestService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<UserRequest>> getAllRequests() {
        List<UserRequest> allRequests = userRequestService.getAllRequests();
        return ResponseEntity.ok(allRequests);
    }

    @PostMapping("/requests")
    public ResponseEntity<String> submitMotorcycleRequest(@Valid @RequestBody SubmitMotorcycleRequestDTO motorcycleRequest) {

        UserRequest request = userRequestService.createNewMotorcycleRequest(motorcycleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(request.getRequestContent());
    }

    @PostMapping("/{motorcycleId}/incorrectValue")
    public ResponseEntity<String> submitIncorrectValueReport(@PathVariable String motorcycleId,
                                                             @RequestBody IncorrectSpecReportDTO requestContent) {
        UserRequest request = userRequestService.createIncorrectValueRequest(requestContent);
        return ResponseEntity.status(HttpStatus.CREATED).body(request.getRequestContent());
    }
}
