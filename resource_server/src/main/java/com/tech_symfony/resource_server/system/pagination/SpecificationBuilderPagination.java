package com.tech_symfony.resource_server.system.pagination;

import com.tech_symfony.resource_server.system.config.JacksonConfig;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Default implementation of the SpecificationBuilderPagination interface.
 * <p>
 * Supports filtering with nested object fields and multiple operators (IN, GT, LT, GTE, LTE).
 */
public interface SpecificationBuilderPagination<T> {
    Specification<T> buildSpecificationFromParams(Map<String, String> params);
}

@Component
class DefaultSpecificationBuilderPagination<T> implements SpecificationBuilderPagination<T> {

    private static final List<String> EXCLUDED_FILTERS = List.of("page", "limit", "sortBy");

    public Specification<T> buildSpecificationFromParams(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((field, value) -> {
                if (shouldExclude(field, value)) return;

                Predicate predicate = buildPredicate(root, criteriaBuilder, field, value);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private boolean shouldExclude(String field, String value) {
        return value == null || value.trim().isEmpty() || EXCLUDED_FILTERS.contains(field);
    }

    private Predicate buildPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String field, String value) {
        boolean isGt = field.endsWith("_gt"), isLt = field.endsWith("_lt"),
                isGte = field.endsWith("_gte"), isLte = field.endsWith("_lte");

        field = field.replaceAll("(_gte|_lte|_gt|_lt)$", "");
        Path<?> path = getPath(root, field);
        if (path == null) return null;

        Class<?> fieldType = path.getJavaType();

        if (isGt || isLt || isGte || isLte) {
            return buildComparisonPredicate(criteriaBuilder, path, value, fieldType, isGt || isGte, isGte || isLte);
        }

        if (value.contains(",")) {
            return buildInPredicate(criteriaBuilder, path, fieldType, Arrays.asList(value.split(",")));
        }

        return buildEqualityPredicate(criteriaBuilder, path, value, fieldType);
    }

    private Predicate buildComparisonPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, String value,
                                               Class<?> fieldType, boolean isGreaterThan, boolean isInclusive) {
        Predicate comparisonPredicate = createComparisonPredicate(criteriaBuilder, path, value, fieldType, isGreaterThan);
        if (comparisonPredicate == null) return null;

        if (isInclusive) {
            Object convertedValue = convertToFieldType(value, fieldType);
            return convertedValue == null ? null : criteriaBuilder.or(comparisonPredicate, criteriaBuilder.equal(path, convertedValue));
        }
        return comparisonPredicate;
    }

    private Predicate buildInPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, Class<?> fieldType, List<String> valuesList) {
        List<Object> convertedValues = new ArrayList<>();
        for (String val : valuesList) {
            Object convertedValue = convertToFieldType(val.trim(), fieldType);
            if (convertedValue != null) {
                convertedValues.add(convertedValue);
            }
        }
        return convertedValues.isEmpty() ? null : path.in(convertedValues);
    }

    private Predicate buildEqualityPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, String value, Class<?> fieldType) {
        Object convertedValue = convertToFieldType(value, fieldType);
        return convertedValue == null ? null : criteriaBuilder.equal(path, convertedValue);
    }

    private Predicate createComparisonPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, String value, Class<?> fieldType, boolean isGreaterThan) {
        try {
            if (Number.class.isAssignableFrom(fieldType)) {
                return buildNumberComparisonPredicate(criteriaBuilder, path, value, fieldType, isGreaterThan);
            } else if (LocalDate.class.isAssignableFrom(fieldType)) {
                LocalDate dateValue = LocalDate.parse(value, JacksonConfig.DATE_FORMATTER);
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<LocalDate>) path, dateValue)
                        : criteriaBuilder.lessThan((Path<LocalDate>) path, dateValue);
            } else if (Instant.class.isAssignableFrom(fieldType)) {
                Instant instantValue = LocalDate.parse(value, JacksonConfig.DATE_FORMATTER)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant();
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<Instant>) path, instantValue)
                        : criteriaBuilder.lessThan((Path<Instant>) path, instantValue);
            } else if (Date.class.isAssignableFrom(fieldType)) {
                Date dateValue = (Date) JacksonConfig.DATE_FORMATTER.parse(value);
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<Date>) path, dateValue)
                        : criteriaBuilder.lessThan((Path<Date>) path, dateValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Predicate buildNumberComparisonPredicate(CriteriaBuilder criteriaBuilder, Path<?> path, String value, Class<?> fieldType, boolean isGreaterThan) {
        try {
            if (Integer.class.isAssignableFrom(fieldType)) {
                Integer numberValue = Integer.parseInt(value);
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<Integer>) path, numberValue)
                        : criteriaBuilder.lessThan((Path<Integer>) path, numberValue);
            } else if (Long.class.isAssignableFrom(fieldType)) {
                Long numberValue = Long.parseLong(value);
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<Long>) path, numberValue)
                        : criteriaBuilder.lessThan((Path<Long>) path, numberValue);
            } else if (Double.class.isAssignableFrom(fieldType)) {
                Double numberValue = Double.parseDouble(value);
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<Double>) path, numberValue)
                        : criteriaBuilder.lessThan((Path<Double>) path, numberValue);
            } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
                BigDecimal numberValue = new BigDecimal(value);
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<BigDecimal>) path, numberValue)
                        : criteriaBuilder.lessThan((Path<BigDecimal>) path, numberValue);
            } else if (BigInteger.class.isAssignableFrom(fieldType)) {
                BigInteger numberValue = new BigInteger(value);
                return isGreaterThan ? criteriaBuilder.greaterThan((Path<BigInteger>) path, numberValue)
                        : criteriaBuilder.lessThan((Path<BigInteger>) path, numberValue);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
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

        return path;
    }

    private Object convertToFieldType(String value, Class<?> fieldType) {
        try {
            // handle date and enum
            if (fieldType.equals(LocalDate.class)) return LocalDate.parse(value, JacksonConfig.DATE_FORMATTER);
            if (fieldType.equals(Instant.class)) {
                LocalDate date = LocalDate.parse(value, JacksonConfig.DATE_FORMATTER);
                return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
            }
            if (fieldType.equals(Date.class)) return new SimpleDateFormat("dd-MM-yyyy").parse(value);
            if (fieldType.isEnum()) return getEnumValue(fieldType, value);

            // Handle basic types using `valueOf` method
            Method valueOfMethod = getStaticValueOfMethod(fieldType);
            if (valueOfMethod != null) {
                return valueOfMethod.invoke(null, value);
            }

            // Handle constructor for non-primitive wrapper types
            if (fieldType.getConstructor(String.class) != null) {
                return fieldType.getConstructor(String.class).newInstance(value);
            }

            // Fallback: if no type-specific conversion is found, return the original string
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Method getStaticValueOfMethod(Class<?> fieldType) {
        try {
            return fieldType.getMethod("valueOf", String.class);
        } catch (NoSuchMethodException e) {
            return null;  // No `valueOf` method found, so we skip this type
        }
    }

    private Number parseNumber(String value, Class<?> fieldType) {
        try {
            if (fieldType.equals(Integer.class)) return Integer.parseInt(value);
            if (fieldType.equals(Long.class)) return Long.parseLong(value);
            if (fieldType.equals(Double.class)) return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getEnumValue(Class<?> enumClass, String value) {
        try {
            return Enum.valueOf((Class<Enum>) enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            try {
                int ordinal = Integer.parseInt(value);
                return ordinal >= 0 && ordinal < enumClass.getEnumConstants().length ? enumClass.getEnumConstants()[ordinal] : null;
            } catch (NumberFormatException ex) {
                return null;
            }
        }
    }
}
