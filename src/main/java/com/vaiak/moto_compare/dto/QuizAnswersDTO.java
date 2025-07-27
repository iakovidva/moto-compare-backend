package com.vaiak.moto_compare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuizAnswersDTO {

    private Experience experience;
    private UseCase useCase;
    private Height height;
    private SizeComfort sizeComfort;
    private List<Feature> features;
    private List<UserPreference> userPreferences;

    public enum Experience {
        @JsonProperty("beginner") BEGINNER,
        @JsonProperty("intermediate") INTERMEDIATE,
        @JsonProperty("advanced") ADVANCED
    }

    public enum UseCase {
        @JsonProperty("commuting") COMMUTING,
        @JsonProperty("touring") TOURING,
        @JsonProperty("sport") SPORT,
        @JsonProperty("adventure") ADVENTURE,
        @JsonProperty("offroad") OFFROAD
    }

    public enum Height {
        @JsonProperty("short") SHORT,
        @JsonProperty("average") AVERAGE,
        @JsonProperty("tall") TALL
    }

    public enum SizeComfort {
        @JsonProperty("lightweight") LIGHTWEIGHT,
        @JsonProperty("medium") MEDIUM,
        @JsonProperty("confident") CONFIDENT
    }

    public enum Feature {
        @JsonProperty("fuel-economy") FUEL_ECONOMY,
        @JsonProperty("quickshifter") QUICKSHIFTER,
        @JsonProperty("abs") ABS,
        @JsonProperty("traction-control") TRACTION_CONTROL,
        @JsonProperty("cruise-control") CRUISE_CONTROL,
        @JsonProperty("none") NONE
    }

    public enum UserPreference {
        @JsonProperty("new-bike") NEW_BIKE,
        @JsonProperty("japan") JAPAN,
        @JsonProperty("europe") EUROPE,
        @JsonProperty("none") NONE
    }
}
