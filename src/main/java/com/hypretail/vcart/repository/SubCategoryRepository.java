package com.hypretail.vcart.repository;

import com.hypretail.vcart.domain.SubCategory;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the SubCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class SubCategoryRepository implements JpaRepository<SubCategory, Long> {
    
    private EntityManager entityManager;


    public SubCategoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public SubCategory mergeAndSave(SubCategory subCategory) {
        subCategory = entityManager.merge(subCategory);
        return save(subCategory);
    }

}
