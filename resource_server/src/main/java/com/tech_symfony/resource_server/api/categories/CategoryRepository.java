package com.tech_symfony.resource_server.api.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
//    @Query("SELECT category FROM Category category "
//            + " WHERE category.parent IS NULL")
    public List<Category> findAllRootsByParentIsNull();
}
