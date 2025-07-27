package com.vaiak.moto_compare.services.quiz;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.stereotype.Component;

import static com.vaiak.moto_compare.dto.QuizAnswersDTO.Experience;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreCentered;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreDirect;
import static com.vaiak.moto_compare.services.quiz.ScoringService.scoreInverse;

@Component
public class ExperienceScoringStrategy implements ScoringStrategy {

    @Override
    public double score(Motorcycle motorcycle, QuizAnswersDTO quizAnswers) {
        Experience experience = quizAnswers.getExperience();
        double hp = motorcycle.getHorsePower();
        double cc = motorcycle.getDisplacement();
//        double weight = moto.getWeight();

        double result = 0;
        switch (experience) {
            case BEGINNER -> {
                result = scoreInverse(hp, 100, 0, 100) +
                            scoreInverse(cc, 100, 0, 800); //+ scoreInverse(weight, 10, 100, 200)
            }
            case INTERMEDIATE -> {
                result = scoreCentered(hp, 100, 70, 120) + // sweet spot: 70–120hp
                            scoreCentered(cc, 100, 500, 800);
            }
            case ADVANCED -> {
                result = scoreDirect(hp, 100, 50, 200) +
                            scoreDirect(cc, 100, 500, 1200);
            }
        };

        return result * getWeight();
    }

    @Override
    public double getWeight() {
        return 0.25;
    }
}
