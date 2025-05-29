package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.UserFavorite;
import com.vaiak.moto_compare.models.UserFavoriteId;
import com.vaiak.moto_compare.repositories.UserFavoriteRepository;
import com.vaiak.moto_compare.security.models.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteService {

    private final UserFavoriteRepository userFavoriteRepository;
    private final UserService userService;
    private final MotorcycleService motorcycleService;

    public UserFavoriteService(UserFavoriteRepository userFavoriteRepository,
                               UserService userService,
                               MotorcycleService motorcycleService) {
        this.userFavoriteRepository = userFavoriteRepository;
        this.userService = userService;
        this.motorcycleService = motorcycleService;
    }

    public List<MotorcycleSummaryDTO> getFavoriteMotorcyclesByUser(Authentication auth) {
        User user = userService.findByEmail(auth.getName());

        List<Motorcycle> motorcycles = userFavoriteRepository.findFavoriteMotorcyclesByUserId(user.getId());
        return motorcycles.stream()
                        .map(MotorcycleMapper::toSummaryDTO)
                .toList();
    }

    public UserFavorite saveFavoriteMotorcycle(Long motorcycleId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());

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
    public void removeFavoriteMotorcycle(Long motorcycleId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());

        userFavoriteRepository.deleteFavoriteMotorcycleForUser(user.getId(), motorcycleId);
    }
}
