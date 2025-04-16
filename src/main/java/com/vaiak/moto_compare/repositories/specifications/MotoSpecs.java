package com.vaiak.moto_compare.repositories.specifications;

import com.vaiak.moto_compare.enums.Category;
import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.data.jpa.domain.Specification;

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
}
