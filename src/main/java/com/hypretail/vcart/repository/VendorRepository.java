package com.hypretail.vcart.repository;

import com.hypretail.vcart.domain.Vendor;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * Micronaut Data  repository for the Vendor entity.
 */
@SuppressWarnings("unused")
@Repository
public abstract class VendorRepository implements JpaRepository<Vendor, Long> {
    
    private EntityManager entityManager;


    public VendorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Vendor mergeAndSave(Vendor vendor) {
        vendor = entityManager.merge(vendor);
        return save(vendor);
    }

}
