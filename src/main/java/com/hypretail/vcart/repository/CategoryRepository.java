package com.hypretail.vcart.repository;

import com.hypretail.vcart.domain.Category;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class CategoryRepository implements JpaRepository<Category, Long> {
    
    private EntityManager entityManager;


    public CategoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Category mergeAndSave(Category category) {
        category = entityManager.merge(category);
        return save(category);
    }

}
