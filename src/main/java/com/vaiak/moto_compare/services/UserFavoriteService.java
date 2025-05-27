package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.UserFavorite;
import com.vaiak.moto_compare.models.UserFavoriteId;
import com.vaiak.moto_compare.repositories.UserFavoriteRepository;
import com.vaiak.moto_compare.security.jwt.JwtTokenProvider;
import com.vaiak.moto_compare.security.models.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteService {

    private final UserFavoriteRepository userFavoriteRepository;
    private final UserService userService;
    private final MotorcycleService motorcycleService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserFavoriteService(UserFavoriteRepository userFavoriteRepository,
                               UserService userService,
                               MotorcycleService motorcycleService,
                               JwtTokenProvider jwtTokenProvider) {
        this.userFavoriteRepository = userFavoriteRepository;
        this.userService = userService;
        this.motorcycleService = motorcycleService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<MotorcycleSummaryDTO> getFavoriteMotorcyclesByUser(String authHeader) {
        User user = getUserFromToken(authHeader);
        List<Motorcycle> motorcycles = userFavoriteRepository.findFavoriteMotorcyclesByUserId(user.getId());
        return motorcycles.stream()
                        .map(MotorcycleMapper::toSummaryDTO)
                .toList();
    }

    public UserFavorite saveFavoriteMotorcycle(Long motorcycleId, String authHeader) {
        User user = getUserFromToken(authHeader);
        Motorcycle motorcycle = motorcycleService.getMotorcycleById(motorcycleId);
        UserFavoriteId userFavoriteId = new UserFavoriteId(user.getId(), motorcycleId);
        UserFavorite userFavorite = UserFavorite.builder()
                .id(userFavoriteId)
                .motorcycle(motorcycle)
                .user(user)
                .build();
        return userFavoriteRepository.save(userFavorite);
    }

    @Transactional
    public void removeFavoriteMotorcycle(Long motorcycleId, String authHeader) {
        User user = getUserFromToken(authHeader);
        userFavoriteRepository.deleteFavoriteMotorcycleForUser(user.getId(), motorcycleId);
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
