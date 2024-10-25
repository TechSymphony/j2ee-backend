package com.tech_symfony.resource_server.system.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;
/**
 *
 * @param <T> Type model
 * @param <VM> View model
 */
public interface PaginationCommand<T, VM> {

    /**
     *
     * @param allParams all query parameters from request
     * @param repository repository
     * @param mapperPagination mapper
     * @param specificationBuilder How jpa query with parameters, can custom according to the needs
     * @return
     */
    Page<VM> execute(Map<String, String> allParams, JpaSpecificationExecutor repository, GenericMapperPagination mapperPagination, SpecificationBuilderPagination specificationBuilder);
}
