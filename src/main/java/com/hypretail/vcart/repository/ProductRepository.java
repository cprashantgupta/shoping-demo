package com.hypretail.vcart.repository;

import com.hypretail.vcart.domain.Product;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;


import javax.persistence.EntityManager;
import javax.transaction.Transactional;
// TODO what is MN equivalent? 
// import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Micronaut Data  repository for the Product entity.
 */
@Repository
public abstract class ProductRepository implements JpaRepository<Product, Long> {
    
    private EntityManager entityManager;


    @Query(value = "select distinct product from Product product left join fetch product.vendors",
        countQuery = "select count(distinct product) from Product product")
    public abstract Page<Product> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct product from Product product left join fetch product.vendors")
    public abstract List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.vendors where product.id =:id")
    public abstract Optional<Product> findOneWithEagerRelationships(Long id);

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Product mergeAndSave(Product product) {
        product = entityManager.merge(product);
        return save(product);
    }

}
