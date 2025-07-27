package com.vaiak.moto_compare.services.quiz;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.vaiak.moto_compare.dto.QuizAnswersDTO.Feature;
import static com.vaiak.moto_compare.dto.QuizAnswersDTO.Feature.FUEL_ECONOMY;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreInverse;

@Component
public class FeaturesScoringStrategy implements ScoringStrategy {

    @Override
    public double score(Motorcycle motorcycle, QuizAnswersDTO quizAnswers) {
        List<Feature> features = quizAnswers.getFeatures();
        if (features.contains(FUEL_ECONOMY)) {
            return scoreInverse(motorcycle.getFuelConsumption() , 100, 2, 9) * getWeight();
        }
        return 0;
    }

    @Override
    public double getWeight() {
        return 0.2;
    }
}
