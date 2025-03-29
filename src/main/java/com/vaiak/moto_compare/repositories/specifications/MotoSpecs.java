package com.vaiak.moto_compare.repositories.specifications;

import com.vaiak.moto_compare.models.Motorcycle;
import org.springframework.data.jpa.domain.Specification;

public class MotoSpecs {

    public static Specification<Motorcycle> hasManufacturer(String manufacturer) {
        return ((root,query,criteriaBuilder) ->
            criteriaBuilder.equal(root.get("manufacturer"), manufacturer));
    }
}
