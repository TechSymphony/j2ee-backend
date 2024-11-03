package com.tech_symfony.resource_server.system.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
@Primary
public class GenericPaginationCommand<T, VM, MP extends GenericMapperPagination<T, VM>> implements PaginationCommand<T, VM> {
    protected Pageable getPagableFromParams(Map<String, String> allParams) {
        Integer page = extractInteger(allParams, "page", 0);
        Integer limit = extractInteger(allParams, "limit", 10);
        String sortBy = allParams.getOrDefault("sortBy", "id");
        String sortDirection = allParams.getOrDefault("sortDirection", "desc");

        if (!isValidSortDirection(sortDirection)) {
            sortDirection = "desc";
        }

        return PageRequest.of(page, limit, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
    }

    private boolean isValidSortDirection(String sortDirection) {
        return "asc".equalsIgnoreCase(sortDirection) || "desc".equalsIgnoreCase(sortDirection);
    }

    @Override
    public Page<VM> execute(Map<String, String> allParams, JpaSpecificationExecutor repository, GenericMapperPagination mapperPagination, SpecificationBuilderPagination specificationBuilder) {

        Pageable paging = this.getPagableFromParams(allParams);

        // Remove pagination and sorting params from allParams, leaving only filters
        allParams.remove("page");
        allParams.remove("limit");
        allParams.remove("sortBy");
        allParams.remove("sortDirection");

        // Build dynamic specification for filtering
        Specification<T> spec = specificationBuilder.buildSpecificationFromParams(allParams);

        // Fetch results with dynamic filters and pagination
        return repository.findAll(spec, paging)
                .map(mapperPagination::entityToViewModelPagination);
    }

    private Integer extractInteger(Map<String, String> params, String key, Integer defaultValue) {
        try {
            return Integer.parseInt(params.getOrDefault(key, defaultValue.toString()));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}