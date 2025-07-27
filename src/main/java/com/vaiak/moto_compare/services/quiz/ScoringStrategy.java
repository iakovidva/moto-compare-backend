package com.vaiak.moto_compare.services.quiz;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.models.Motorcycle;

public interface ScoringStrategy {
    double score(Motorcycle motorcycle, QuizAnswersDTO quizAnswers); //should be in range 0-100
    double getWeight();
}
