package com.tech_symfony.resource_server.system.pagination;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of the SpecificationBuilderPagination interface.
 *
 * @Example {
 * "id": 3,
 * "user": {
 * "id": 5,
 * "fullName": "User",
 * "email": "user@example.com",
 * "phone": "987654321",
 * "createdAt": "2024-09-19T15:14:19.848798Z",
 * "updatedAt": "2024-09-19T15:14:19.848798Z",
 * "role": null,
 * "enabled": true,
 * "username": "user"
 * }
 * }
 * filter using the name key value
 * ?id=3&user.id=5 : work despite the nested object
 * ?id=1,2,3,4: work by using sql id IN(1,2,3,4)
 */
public interface SpecificationBuilderPagination<T> {
    Specification<T> buildSpecificationFromParams(Map<String, String> params);
}

@Component
class DefaultSpecificationBuilderPagination<T> implements SpecificationBuilderPagination<T> {

    // Fields that should not be part of the filtering
    private static final List<String> EXCLUDED_FILTERS = List.of("page", "limit", "sortBy");

    public Specification<T> buildSpecificationFromParams(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();

                if (value == null || value.trim().isEmpty() || EXCLUDED_FILTERS.contains(field)) {
                    continue;
                }
                value = value.trim();

                // Handle dynamic fields, e.g., "user.id", "user.fullName"
                Path<?> path = getPath(root, field);
                Class<?> fieldType = path.getJavaType();

                // Check if the value is a comma-separated list
                if (value.contains(",")) {
                    List<String> valuesList = Arrays.asList(value.split(","));
                    if (path != null) {
                        // Create IN predicate
                        if (fieldType.isEnum()) {
                            List<Object> enumValues = new ArrayList<>();
                            for (String val : valuesList) {
                                Object enumValue = getEnumValue(fieldType, val);
                                if (enumValue != null) {
                                    enumValues.add(enumValue);
                                }
                            }
                            if (!enumValues.isEmpty()) {
                                predicates.add(path.in(enumValues));
                            }
                        } else predicates.add(path.in(valuesList));
                    }
                    continue;
                }


                if (path == null) {
                    continue; // If the path is invalid, skip this entry
                }
                if (isStringField(path)) {
                    predicates.add(criteriaBuilder.like((Path<String>) path, "%" + value + "%")); // Partial match for strings
                } else if (fieldType.isEnum()) {
                    // Handle enums
                    // name, string and integer case , pass 3 case.
                    Object enumValue = getEnumValue(fieldType, value);
                    if (enumValue != null) {
                        predicates.add(criteriaBuilder.equal(path, enumValue)); // Exact match for enums
                    }
                } else {
                    predicates.add(criteriaBuilder.equal(path, value)); // Exact match for other types
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Path<?> getPath(Root<T> root, String field) {
        String[] fieldParts = field.split("\\.");
        Path<?> path = root;

        for (String part : fieldParts) {
            try {
                path = path.get(part);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return path; // Return the resolved path
    }

    private boolean isStringField(Path<?> path) {
        return String.class.equals(path.getJavaType());
    }


    private Object getEnumValue(Class<?> enumClass, String value) {
        try {
            // Check if value can be parsed as an integer
            int ordinal = Integer.parseInt(value);
            // Return the enum based on ordinal
            return getEnumByOrdinal(enumClass, ordinal);
        } catch (NumberFormatException e) {
            // If it's not an integer, try to get the enum by name
            return getEnumByName(enumClass, value);
        }
    }

    private Object getEnumByOrdinal(Class<?> enumClass, int ordinal) {
        if (enumClass.isEnum()) {
            Object[] enumConstants = enumClass.getEnumConstants();
            if (ordinal >= 0 && ordinal < enumConstants.length) {
                return enumConstants[ordinal]; // Return the enum by its ordinal
            }
        }
        return null; // Invalid ordinal
    }

    private Object getEnumByName(Class<?> enumClass, String name) {
        if (enumClass.isEnum()) {
            try {
                return Enum.valueOf((Class<Enum>) enumClass, name.toUpperCase()); // Use uppercase for case-insensitive match
            } catch (IllegalArgumentException e) {
                return null; // Return null if no match found
            }
        }
        return null; // Not an enum class
    }
}
