package com.tech_symfony.resource_server.system.pagination;

import org.springframework.data.domain.Page;

import java.util.Map;
/**
 *
 * @param <T> Type model
 * @param <VM> View model
 */
public interface PaginationCommand<T, VM> {
    Page<VM> execute(Map<String, String> allParams);
}
