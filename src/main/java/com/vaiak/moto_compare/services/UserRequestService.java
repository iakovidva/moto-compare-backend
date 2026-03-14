package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.SubmitFeedbackRequestDTO;
import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.mappers.SubmitRequestMapper;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.repositories.UserRequestRepository;
import com.vaiak.moto_compare.security.models.User;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserRequestService {

    private final UserRequestRepository repository;
    private final UserService userService;
    private final MeterRegistry meterRegistry;

    public UserRequestService(UserRequestRepository repository,
                              UserService userService,
                              MeterRegistry meterRegistry) {
        this.repository = repository;
        this.userService = userService;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public UserRequest createNewMotorcycleRequest(SubmitMotorcycleRequestDTO request, @Nullable Authentication auth) {
        User user;
        if (auth != null) {
            user = userService.findByEmailOptional(auth.getName()).orElse(null);
        } else {
            user = null;
        }

        UserRequest userRequest = SubmitRequestMapper.toUserRequest(request, user);
        incrementRequestCounter("new_motorcycle", auth != null);
        return createRequest(userRequest);
    }

    @Transactional
    public UserRequest createIncorrectValueRequest(IncorrectSpecReportDTO request, @Nullable Authentication auth) {
        User user;
        if (auth != null) {
            user = userService.findByEmailOptional(auth.getName()).orElse(null);
        } else {
            user = null;
        } //TODO consider using Optional.ofNullable()
        UserRequest userRequest = SubmitRequestMapper.toUserRequest(request, user);
        incrementRequestCounter("incorrect_value", auth != null);
        return createRequest(userRequest);
    }

    @Transactional
    public UserRequest submitFeedbackRequest(SubmitFeedbackRequestDTO feedback, @Nullable Authentication auth) {
        User user;
        if (auth != null) {
            user = userService.findByEmailOptional(auth.getName()).orElse(null);
        } else {
            user = null;
        }
        UserRequest userRequest = SubmitRequestMapper.feedbackToUserRequest(feedback, user);
        incrementRequestCounter("feedback", auth != null);
        return createRequest(userRequest);
    }

    private void incrementRequestCounter(String requestType, boolean authenticated) {
        meterRegistry.counter(
                "app_user_requests_total",
                "request_type", requestType,
                "authenticated", String.valueOf(authenticated)
        ).increment();
    }

    public UserRequest createRequest(UserRequest userRequest) {
        return repository.save(userRequest);
    }

    public List<UserRequest> getAllRequests() {
        return repository.findAll();
    }

    public List<UserRequest> getRequestsFromUserId(UUID userId) {
        return repository.getRequestsForUser(userId);
    }
}
