package com.vaiak.moto_compare.controllers;

import com.vaiak.moto_compare.dto.UserDTO;
import com.vaiak.moto_compare.dto.review.ReviewResponseDTO;
import com.vaiak.moto_compare.models.UserRequest;
import com.vaiak.moto_compare.security.jwt.JwtTokenProvider;
import com.vaiak.moto_compare.security.models.User;
import com.vaiak.moto_compare.services.ReviewService;
import com.vaiak.moto_compare.services.UserRequestService;
import com.vaiak.moto_compare.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRequestService userRequestService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReviewService reviewService;

    public UserController(UserService userService,
                          UserRequestService userRequestService,
                          JwtTokenProvider jwtTokenProvider,
                          ReviewService reviewService) {
        this.userService = userService;
        this.userRequestService = userRequestService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.reviewService = reviewService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<UserDTO> getUserDTO(@RequestHeader("Authorization") String authHeader) {
        User user = getUserFromToken(authHeader);
        UserDTO userDTO = new UserDTO(user.getUserName(), user.getEmail(), user.getCreatedAt());
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<List<ReviewResponseDTO>> getUserReviews(@RequestHeader("Authorization") String authHeader) {
        User user = getUserFromToken(authHeader);
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByUserId(user.getId());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<List<UserRequest>> getUserRequests(@RequestHeader("Authorization") String authHeader) { //TODO create UserRequestDTO.
        User user = getUserFromToken(authHeader);
        List<UserRequest> requests = userRequestService.getRequestsFromUserId(user.getId());
        return ResponseEntity.ok(requests);
    }

    private User getUserFromToken(String authHeader) {
        String token = extractToken(authHeader);
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userService.findByEmail(email);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}
