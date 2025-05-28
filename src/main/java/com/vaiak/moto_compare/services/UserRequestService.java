package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.IncorrectSpecReportDTO;
import com.vaiak.moto_compare.dto.motorcycle.SubmitMotorcycleRequestDTO;
import com.vaiak.moto_compare.mappers.SubmitRequestMapper;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.repositories.UserRequestRepository;
import com.vaiak.moto_compare.security.jwt.JwtTokenProvider;
import com.vaiak.moto_compare.security.models.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserRequestService {

    private final UserRequestRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public UserRequestService(UserRequestRepository repository, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.repository = repository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Transactional
    public UserRequest createNewMotorcycleRequest(SubmitMotorcycleRequestDTO request, String authHeader) {
        User user; //TODO refactor that ugly code :)
        try {
            String email = jwtTokenProvider.getEmailFromToken(authHeader.substring(7));
            user = userService.findByEmail(email);
        } catch (RuntimeException e) {
          user = null;
        }

        UserRequest userRequest = SubmitRequestMapper.toUserRequest(request, user);
        return createRequest(userRequest);
    }

    @Transactional
    public UserRequest createIncorrectValueRequest(IncorrectSpecReportDTO request, String authHeader) {
        User user;  //TODO refactor that ugly code :)
        try {
            String email = jwtTokenProvider.getEmailFromToken(authHeader.substring(7));
            user = userService.findByEmail(email);
        } catch (RuntimeException e) {
            user = null;
        }
        UserRequest userRequest = SubmitRequestMapper.toUserRequest(request, user);
        return createRequest(userRequest);
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
