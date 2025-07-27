package com.vaiak.moto_compare.services.quiz;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoringService {

    // UseCase 30%, Experience 25%, Fuel economy 20%, SizeComfort 13%, Height 12%.
    private final List<ScoringStrategy> strategies;

    public ScoringService(List<ScoringStrategy> strategies) {
        this.strategies = strategies;
    }

    public double calculateTotalScore(Motorcycle moto, QuizAnswersDTO quizAnswers) {
        return strategies.stream()
                .mapToDouble(s -> s.score(moto, quizAnswers))
                .sum();
    }

    public static double scoreInverse(double value, double maxScore, double min, double max) {
        value = Math.max(min, Math.min(value, max));
        return maxScore * (1 - (value - min) / (max - min));
    }

    public static double scoreDirect(double value, double maxScore, double min, double max) {
        value = Math.max(min, Math.min(value, max));
        return maxScore * (value - min) / (max - min);
    }

    public static double scoreCentered(double value, double maxScore, double idealMin, double idealMax) {
        if (value < idealMin) {
            return scoreDirect(value, maxScore, idealMin - 50, idealMin);
        } else if (value > idealMax) {
            return scoreInverse(value, maxScore, idealMax, idealMax + 50);
        } else {
            return maxScore;
        }
    }
}
