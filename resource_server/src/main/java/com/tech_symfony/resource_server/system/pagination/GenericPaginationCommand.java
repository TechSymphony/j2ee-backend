package com.tech_symfony.resource_server.system.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

@RequiredArgsConstructor
public class GenericPaginationCommand<T, VM, MP extends GenericMapperPagination<T, VM>> implements PaginationCommand<T, VM> {

    private final JpaSpecificationExecutor<T> repository;
    private final MP mapper; // MapStruct mapper
    private final SpecificationBuilderPagination<T> specificationBuilder;


    @Override
    public Page<VM> execute(Map<String, String> allParams) {
        // Extract pagination and sorting parameters
        Integer page = extractInteger(allParams, "page", 0);
        Integer limit = extractInteger(allParams, "limit", 10);
        String sortBy = allParams.getOrDefault("sortBy", "id");

        Pageable paging = PageRequest.of(page, limit, Sort.by(Sort.Order.desc(sortBy)));

        // Remove pagination and sorting params from allParams, leaving only filters
        allParams.remove("page");
        allParams.remove("limit");
        allParams.remove("sortBy");

        // Build dynamic specification for filtering
        Specification<T> spec = specificationBuilder.buildSpecificationFromParams(allParams);

        // Fetch results with dynamic filters and pagination
        return repository.findAll(spec, paging)
                .map(mapper::entityToViewModelPagination);
    }

    private Integer extractInteger(Map<String, String> params, String key, Integer defaultValue) {
        try {
            return Integer.parseInt(params.getOrDefault(key, defaultValue.toString()));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}