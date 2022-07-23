package com.hypretail.vcart.repository;

import com.hypretail.vcart.domain.Purchaser;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the Purchaser entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class PurchaserRepository implements JpaRepository<Purchaser, Long> {
    
    private EntityManager entityManager;


    public PurchaserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Purchaser mergeAndSave(Purchaser purchaser) {
        purchaser = entityManager.merge(purchaser);
        return save(purchaser);
    }

}
