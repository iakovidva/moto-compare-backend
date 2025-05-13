package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.mappers.SubmitRequestMapper;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.repositories.UserRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRequestService {

    private final UserRequestRepository repository;

    public UserRequestService(UserRequestRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserRequest createNewMotorcycleRequest(SubmitMotorcycleRequestDTO request) {
        UserRequest userRequest = SubmitRequestMapper.toUserRequest(request);
        return createRequest(userRequest);
    }

    @Transactional
    public UserRequest createIncorrectValueRequest(IncorrectSpecReportDTO request) {
        UserRequest userRequest = SubmitRequestMapper.toUserRequest(request);
        return createRequest(userRequest);
    }

    public UserRequest createRequest(UserRequest userRequest) {
        return repository.save(userRequest);
    }

    public List<UserRequest> getAllRequests() {
        return repository.findAll();
    }
}
