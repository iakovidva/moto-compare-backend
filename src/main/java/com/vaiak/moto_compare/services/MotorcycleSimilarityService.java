package com.vaiak.moto_compare.services;

import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MotorcycleSimilarityService {

    public List<Motorcycle> findSimilarMotorcycles(List<Motorcycle> candidates, Motorcycle reference, int limit) {
        return candidates.stream()
                .map(candidate -> new ScoredMotorcycle(candidate, calculateSimilarityScore(candidate, reference)))
                .sorted(Comparator.comparingDouble(ScoredMotorcycle::score).reversed())
                .limit(limit)
                .map(ScoredMotorcycle::motorcycle)
                .toList();
    }

    private double calculateSimilarityScore(Motorcycle candidate, Motorcycle reference) {
        double score = 0;

        // Category match (highest priority - 40 points)
        if (candidate.getCategory() == reference.getCategory()) {
            score += 40;
        }

        // Displacement similarity (30 points max)
        score += Math.max(0, 30 - (Math.abs(candidate.getDisplacement() - reference.getDisplacement()) * 30.0 / 500));

        // Power similarity (20 points max)
        score += Math.max(0, 20 - (Math.abs(candidate.getHorsePower() - reference.getHorsePower()) * 20.0 / 50));

        // Weight similarity (15 points max)
        score += Math.max(0, 15 - (Math.abs(candidate.getWeight() - reference.getWeight()) * 15.0 / 100));

        // Seat height similarity (10 points max)
        score += calculateSeatHeightScore(candidate.getSeatHeight(), reference.getSeatHeight());

        // Fuel consumption similarity (8 points max)
        score += Math.max(0, 8 - (Math.abs(candidate.getFuelConsumption() - reference.getFuelConsumption()) * 8.0 / 3));

        // ABS preference (5 points)
        if (candidate.isAbs() == reference.isAbs()) {
            score += 5;
        }

        // Tank capacity similarity (4 points max)
        score += Math.max(0, 4 - (Math.abs(candidate.getTankCapacity() - reference.getTankCapacity()) * 4.0 / 10));

        // Traction control preference (3 points)
        if (candidate.isTractionControl() == reference.isTractionControl()) {
            score += 3;
        }

        return score;
    }

    private double calculateSeatHeightScore(String candidateSeatHeight, String referenceSeatHeight) {
        try {
            if (candidateSeatHeight != null && referenceSeatHeight != null) {
                int candidateHeight = extractSeatHeight(candidateSeatHeight);
                int referenceHeight = extractSeatHeight(referenceSeatHeight);
                return Math.max(0, 10 - (Math.abs(candidateHeight - referenceHeight) * 10.0 / 100));
            }
        } catch (Exception e) {
            // If parsing fails, give neutral score
        }
        return 5; // Neutral score when data is missing
    }

    private int extractSeatHeight(String seatHeight) {
        // Extract first number from seat height string (e.g., "840mm-860mm" -> 840)
        return Integer.parseInt(seatHeight.replaceAll("[^0-9].*", ""));
    }

    private record ScoredMotorcycle(Motorcycle motorcycle, double score) {}
}
