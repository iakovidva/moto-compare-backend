package com.vaiak.moto_compare.repositories.specifications;

import com.vaiak.moto_compare.dto.QuizAnswersDTO;
import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.enums.Manufacturer;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MotoSpecs {

    public static Specification<Motorcycle> hasManufacturer(String manufacturer) {
        return ((root,query,criteriaBuilder) ->
            criteriaBuilder.equal(root.get("manufacturer"), manufacturer));
    }

    public static Specification<Motorcycle> isCategory(Category category) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category));
    }

    public static Specification<Motorcycle> hasHorsePowerMin(int horsePowerMin) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("horsePower"), horsePowerMin));
    }

    public static Specification<Motorcycle> hasHorsePowerMax(int horsePowerMax) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("horsePower"), horsePowerMax));
    }

    public static Specification<Motorcycle> hasDisplacementMin(int displacementMin) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("displacement"), displacementMin));
    }

    public static Specification<Motorcycle> hasDisplacementMax(int displacementMax) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("displacement"), displacementMax));
    }

    public static Specification<Motorcycle> hasYearMin(int yearMin) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("yearRange"), yearMin));
    }

    public static Specification<Motorcycle> hasYearMax(int yearMax) {
        return ((root,query,criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("yearRange"), yearMax));
    }

    public static Specification<Motorcycle> fromQuizAnswers(QuizAnswersDTO answers) {
        Specification<Motorcycle> spec = Specification.where(null); // start empty

        if ( !answers.getUserPreferences().contains(QuizAnswersDTO.UserPreference.NONE) ) {
            // Hard filter 1: Manufacturer region
            List<Manufacturer> manufacturers;
            if (answers.getUserPreferences().contains(QuizAnswersDTO.UserPreference.JAPAN)) {
                manufacturers = new ArrayList<>(List.of(Manufacturer.HONDA, Manufacturer.YAMAHA, Manufacturer.KAWASAKI, Manufacturer.SUZUKI));
                spec = spec.and(((root,query,criteriaBuilder) -> root.get("manufacturer").in(manufacturers)));
            }
            else if (answers.getUserPreferences().contains(QuizAnswersDTO.UserPreference.EUROPE)) {
                manufacturers = new ArrayList<>(List.of(Manufacturer.APRILIA, Manufacturer.BMW, Manufacturer.DUCATI, Manufacturer.KTM, Manufacturer.BENELLI, Manufacturer.TRIUMPH,
                        Manufacturer.HUSQVARNA, Manufacturer.MOTO_GUZZI, Manufacturer.MV_AGUSTA, Manufacturer.PIAGGIO));
                spec = spec.and(((root,query,criteriaBuilder) -> root.get("manufacturer").in(manufacturers)));
            }

            // Hard filter 2: Only new bikes
            if (answers.getUserPreferences().contains(QuizAnswersDTO.UserPreference.NEW_BIKE)) {
                spec = spec.and(MotoSpecs.hasYearMin(2020));
            }
        }

        // Hard filter 3: Features
        if ( !answers.getFeatures().contains(QuizAnswersDTO.Feature.NONE) ) {
            if (answers.getFeatures().contains(QuizAnswersDTO.Feature.ABS)) {
                spec = spec.and((root, query, cb) -> cb.isTrue(root.get("abs")));
            }

            if (answers.getFeatures().contains(QuizAnswersDTO.Feature.TRACTION_CONTROL)) {
                spec = spec.and((root, query, cb) -> cb.isTrue(root.get("tractionControl")));
            }

            if (answers.getFeatures().contains(QuizAnswersDTO.Feature.CRUISE_CONTROL)) {
                spec = spec.and((root, query, cb) -> cb.isTrue(root.get("cruiseControl")));
            }

            if (answers.getFeatures().contains(QuizAnswersDTO.Feature.QUICKSHIFTER)) {
                spec = spec.and((root, query, cb) -> cb.notEqual(root.get("quickShifter"), "None"));
            }
        }

        // Hard filter 4: HP and CC for a Beginner
        if (answers.getExperience().equals(QuizAnswersDTO.Experience.BEGINNER)) {
            spec = spec.and(((root,query,cb) ->
                    cb.lessThanOrEqualTo(root.get("horsePower"), 60)))
                    .and((root,query,cb) ->
                            cb.lessThanOrEqualTo(root.get("displacement"), 800));
        }

        // Hard filter 4: Weight for lightweight
        if (answers.getSizeComfort().equals(QuizAnswersDTO.SizeComfort.LIGHTWEIGHT)) {
            spec = spec.and((root,query,cb) ->
                    cb.lessThanOrEqualTo(root.get("weight"), 200 ));
        }

        // Hard filter 5: Fuel economy
        if (answers.getFeatures().contains(QuizAnswersDTO.Feature.FUEL_ECONOMY)) {
            spec = spec.and((root,query,cb) ->
                    cb.lessThanOrEqualTo(root.get("fuelConsumption"), 6 ));
        }

        // Hard filter 6: Use case
        List<Category> offRoadCategories = List.of(Category.ADVENTURE, Category.DUAL_SPORT, Category.ENDURO, Category.SCRAMBLER, Category.MOTOCROSS);

        if (answers.getUseCase().equals(QuizAnswersDTO.UseCase.OFFROAD)) {
            spec = spec.and((root,query,cb) ->
                    root.get("category").in(offRoadCategories));
        }
        else if (answers.getUseCase().equals(QuizAnswersDTO.UseCase.SPORT)) {
            spec = spec.and((root,query,cb) ->
                    cb.not(root.get("category").in(offRoadCategories)));
        }

        return spec;
    }

}
