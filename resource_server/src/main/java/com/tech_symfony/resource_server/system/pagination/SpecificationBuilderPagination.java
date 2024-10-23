package com.tech_symfony.resource_server.system.pagination;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SpecificationBuilderPagination<T> {

    // Fields that should not be part of the filtering
    private static final List<String> EXCLUDED_FILTERS = List.of("page", "limit", "sortBy");

    public Specification<T> buildSpecificationFromParams(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();

                if (value == null || value.trim().isEmpty() || EXCLUDED_FILTERS.contains(field) || !isFieldValid(root, field)) {
                    continue;
                }
                value = value.trim();

                if (isStringField(root, field)) {
                    predicates.add(criteriaBuilder.like(root.get(field), "%" + value + "%")); // Partial match for strings
                } else {
                    predicates.add(criteriaBuilder.equal(root.get(field), value)); // Exact match for other types
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private boolean isFieldValid(Root<T> root, String field) {
        try {
            root.get(field);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isStringField(Root<T> root, String field) {
        try {
            return String.class.equals(root.get(field).getJavaType());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
