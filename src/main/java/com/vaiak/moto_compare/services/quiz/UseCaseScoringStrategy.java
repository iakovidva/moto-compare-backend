package com.vaiak.moto_compare.services.quiz;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.vaiak.moto_compare.dto.QuizAnswersDTO.UseCase;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreCentered;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreDirect;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreInverse;

@Component
public class UseCaseScoringStrategy implements ScoringStrategy {

    private static final Map<UseCase, Set<Category>> preferredCategories = Map.of(
            UseCase.COMMUTING, Set.of(Category.SCOOTER, Category.UNDERBONE, Category.NAKED, Category.ELECTRIC),
            UseCase.TOURING, Set.of(Category.TOURING, Category.SPORT_TOURING, Category.CRUISER, Category.ADVENTURE),
            UseCase.SPORT, Set.of(Category.SUPERSPORT, Category.SPORT_TOURING),
            UseCase.ADVENTURE, Set.of(Category.ADVENTURE, Category.DUAL_SPORT, Category.ENDURO, Category.TOURING),
            UseCase.OFFROAD, Set.of(Category.ENDURO, Category.MOTOCROSS, Category.SCRAMBLER, Category.DUAL_SPORT)
    );

    @Override
    public double score(Motorcycle motorcycle, QuizAnswersDTO quizAnswers) {
        UseCase useCase = quizAnswers.getUseCase();
        Category category = motorcycle.getCategory();
        double fuel = motorcycle.getFuelConsumption();
        int hp = motorcycle.getHorsePower();
        int tank = motorcycle.getTankCapacity();
        int weight = motorcycle.getWeight();

        double score = 0;

        switch (useCase) {
            case COMMUTING -> {
                score += scoreInverse(fuel, 40, 2.5, 6.5); // Lower fuel is better
                score += scoreInverse(weight, 20, 100, 220); // Light bikes preferred
                score += scoreInverse(hp, 20, 15, 70); // Too much power is wasteful
                score += scoreCategory(category, preferredCategories.get(useCase), 20);
            }
            case TOURING -> {
                score += scoreDirect(tank, 25, 15, 30); // Big tank = longer trips
                score += scoreDirect(weight, 15, 180, 300); // Heavier = stable on highways
                score += scoreCentered(hp, 15, 70, 110); // 70–110hp ideal for touring
                score += scoreCategory(category, preferredCategories.get(useCase), 20);
            }
            case SPORT -> {
                score += scoreDirect(hp, 35, 70, 200); // High power is key
                score += scoreInverse(weight, 15, 120, 250); // Lighter = better handling
                score += scoreCategory(category, preferredCategories.get(useCase), 20);
            }
            case ADVENTURE -> {
                score += scoreDirect(tank, 20, 15, 25); // Fuel range matters
                score += scoreCentered(hp, 20, 60, 100); // Balanced power
                score += scoreCentered(weight, 15, 160, 220); // Not too heavy, not too light
                score += scoreCategory(category, preferredCategories.get(useCase), 25);
            }
            case OFFROAD -> {
                score += scoreInverse(weight, 30, 90, 160); // Lightweight is crucial
                score += scoreCentered(hp, 15, 30, 60); // You need manageable power
                score += scoreCategory(category, preferredCategories.get(useCase), 25);
            }
        }

        return score * getWeight();
    }


    private double scoreCategory(Category actual, Set<Category> preferred, int maxPoints) {
        if (preferred.contains(actual)) return maxPoints;
        return 0;
    }

    @Override
    public double getWeight() {
        return 0.3;
    }
}
