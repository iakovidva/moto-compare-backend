package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.dto.motorcycle.MotorcycleSummaryDTO;
import com.vaiak.moto_compare.mappers.MotorcycleMapper;
import com.vaiak.moto_compare.models.Motorcycle;
import com.vaiak.moto_compare.models.UserFavorite;
import com.vaiak.moto_compare.models.UserFavoriteId;
import com.vaiak.moto_compare.repositories.UserFavoriteRepository;
import com.vaiak.moto_compare.security.models.User;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteService {

    private final UserFavoriteRepository userFavoriteRepository;
    private final UserService userService;
    private final MotorcycleService motorcycleService;
    private final MeterRegistry meterRegistry;

    public UserFavoriteService(UserFavoriteRepository userFavoriteRepository,
                               UserService userService,
                               MotorcycleService motorcycleService,
                               MeterRegistry meterRegistry) {
        this.userFavoriteRepository = userFavoriteRepository;
        this.userService = userService;
        this.motorcycleService = motorcycleService;
        this.meterRegistry = meterRegistry;
    }

    public List<MotorcycleSummaryDTO> getFavoriteMotorcyclesByUser(Authentication auth) {
        User user = userService.findByEmail(auth.getName());

        List<Motorcycle> motorcycles = userFavoriteRepository.findFavoriteMotorcyclesByUserId(user.getId());
        return motorcycles.stream()
                        .map(MotorcycleMapper::toSummaryDTO)
                .toList();
    }

    public UserFavorite saveFavoriteMotorcycle(Long motorcycleId, Authentication auth) {
        try {
            User user = userService.findByEmail(auth.getName());

            Motorcycle motorcycle = motorcycleService.getMotorcycleById(motorcycleId);
            UserFavoriteId userFavoriteId = new UserFavoriteId(user.getId(), motorcycleId);
            UserFavorite userFavorite = UserFavorite.builder()
                    .id(userFavoriteId)
                    .motorcycle(motorcycle)
                    .user(user)
                    .build();
            UserFavorite saved = userFavoriteRepository.save(userFavorite);
            meterRegistry.counter("app_favorite_actions_total", "action", "add", "result", "success").increment();
            meterRegistry.counter(
                    "moto_interest_manufacturer_total",
                    "manufacturer", motorcycle.getManufacturer().name(),
                    "source", "favorites_add"
            ).increment();
            meterRegistry.counter(
                    "moto_interest_category_total",
                    "category", motorcycle.getCategory().name(),
                    "source", "favorites_add"
            ).increment();
            return saved;
        } catch (RuntimeException ex) {
            meterRegistry.counter("app_favorite_actions_total", "action", "add", "result", "failure").increment();
            throw ex;
        }
    }

    @Transactional
    public void removeFavoriteMotorcycle(Long motorcycleId, Authentication auth) {
        try {
            User user = userService.findByEmail(auth.getName());
            userFavoriteRepository.deleteFavoriteMotorcycleForUser(user.getId(), motorcycleId);
            meterRegistry.counter("app_favorite_actions_total", "action", "remove", "result", "success").increment();
        } catch (RuntimeException ex) {
            meterRegistry.counter("app_favorite_actions_total", "action", "remove", "result", "failure").increment();
            throw ex;
        }
    }
}
