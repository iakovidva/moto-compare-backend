package com.vaiak.moto_compare.services.quiz;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.stereotype.Component;

import static com.vaiak.moto_compare.dto.QuizAnswersDTO.SizeComfort;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreCentered;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreDirect;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreInverse;

@Component
public class SizeComfortScoringStrategy implements ScoringStrategy {

    @Override
    public double score(Motorcycle motorcycle, QuizAnswersDTO quizAnswers) {
        int weight = motorcycle.getWeight();
        int groundClearance = motorcycle.getGroundClearance();
        SizeComfort sizeComfort = quizAnswers.getSizeComfort();
        double result = 0;
        switch (sizeComfort) {
            case LIGHTWEIGHT -> {
                // Riders who prefer lightweight bikes favor low weight and low ground clearance (more control)
                result = scoreInverse(weight, 100, 100, 250) +  // penalize heavy bikes
                        scoreInverse(groundClearance, 100, 100, 250); // prefer lower ground clearance
            }
            case MEDIUM -> {
                // Medium comfort riders want moderate values; score centered
                result = scoreCentered(weight, 100, 170, 230) +
                        scoreCentered(groundClearance, 100, 170, 230);
            }
            case CONFIDENT -> {
                // Confident riders might prefer larger/taller/heavier bikes
                result = scoreDirect(weight, 100, 150, 300) +
                        scoreDirect(groundClearance, 100, 150, 300);
            }
        }

        return result * getWeight(); // Apply the relative importance of this strategy
    }

    @Override
    public double getWeight() {
        return 0.13;
    }
}
