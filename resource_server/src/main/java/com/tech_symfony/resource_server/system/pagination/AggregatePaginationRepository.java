package com.tech_symfony.resource_server.system.pagination;

import com.tech_symfony.resource_server.system.model.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;


public interface AggregatePaginationRepository<T extends BaseEntity> {
    <S extends Number> S sum(Specification<T> spec, Class<T> entityClass, Class<S> resultType, String fieldName);
//    public BigDecimal sum(Specification<T> spec, Class<T> entityClass, Class<BigDecimal> resultType, String fieldName);

}


@RequiredArgsConstructor
class AggregatePaginationRepositoryImpl<T extends BaseEntity> implements AggregatePaginationRepository<T> {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public <S extends Number> S sum(Specification<T> spec, Class<T> entityClass, Class<S> resultType, String fieldName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<S> query = builder.createQuery(resultType);

        Root<T> from = query.from(entityClass);
        if ( spec != null )
            query.where(spec.toPredicate(from, query, builder));

        query.select(builder.sum(from.get(fieldName)));

        return entityManager.createQuery(query).getSingleResult();
    }


}
