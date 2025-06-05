package com.propertyhub.specification;

import com.propertyhub.model.Property;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PropertySpecification {

    public static Specification<Property> fromFilterMap(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((key, value) -> {
                if (!StringUtils.hasText(value)) {
                    return; // Skip if value is empty or null
                }
                try {
                    switch (key) {
                        case "location":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + value.toLowerCase() + "%"));
                            break;
                        case "minPrice":
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(value)));
                            break;
                        case "maxPrice":
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), Double.parseDouble(value)));
                            break;
                        case "bedrooms":
                            predicates.add(criteriaBuilder.equal(root.get("bedrooms"), Integer.parseInt(value)));
                            break;
                        case "propertyType":
                            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("propertyType")), value.toLowerCase()));
                            break;
                        case "listingType":
                            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("listingType")), value.toLowerCase()));
                            break;
                        // Add more cases for other filterable fields as needed
                    }
                } catch (NumberFormatException e) {
                    // Log or handle parsing error, or simply ignore invalid filter value
                    System.err.println("Could not parse filter value for key '" + key + "': " + value);
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
