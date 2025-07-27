package com.vaiak.moto_compare.services.quiz;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.stereotype.Component;

import static com.vaiak.moto_compare.dto.QuizAnswersDTO.Height;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreCentered;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreDirect;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreInverse;


@Component
public class HeightScoringStrategy implements ScoringStrategy {

    @Override
    public double score(Motorcycle motorcycle, QuizAnswersDTO quizAnswers) {
        Height height = quizAnswers.getHeight();
        double seatHeight = parseSeatHeight(motorcycle.getSeatHeight()); // Convert to single value
        int groundClearance = motorcycle.getGroundClearance();
        double result = 0;
        switch (height) {
            case SHORT -> {
                // Lower seat height preferred
                result = scoreInverse(seatHeight, 100, 700, 850) +
                        scoreInverse(groundClearance, 100, 100, 220);
            }
            case AVERAGE -> {
                // Middle range preferred
                result = scoreCentered(seatHeight, 100, 800, 880) +
                        scoreCentered(groundClearance, 100, 150, 250);
            }
            case TALL -> {
                // Higher seat/clearance preferred
                result = scoreDirect(seatHeight, 100, 820, 950) +
                        scoreDirect(groundClearance, 100, 200, 300);
            }
        }

        return result * getWeight();
    }

    @Override
    public double getWeight() {
        return 0.12;
    }

    private double parseSeatHeight(String seatHeightStr) {
        try {
            if (seatHeightStr.contains("-")) {
                String[] parts = seatHeightStr.split("-");
                return extractNumber(parts[0]);
            } else {
                return extractNumber(seatHeightStr);
            }
        } catch (NumberFormatException e) {
            // Log this properly in production
            System.err.println("Invalid seat height format: " + seatHeightStr);
            return 850; // Fallback value
        }
    }

    private double extractNumber(String input) {
        // Remove all non-digit and non-decimal-point characters
        String numeric = input.replaceAll("[^\\d.]", "");
        return Double.parseDouble(numeric);
    }
}
