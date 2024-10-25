package com.tech_symfony.resource_server.system.pagination;



public interface GenericMapperPagination<E, V> {
    V entityToViewModelPagination(E entity);
}