package com.hypretail.vcart.service;

import com.hypretail.vcart.service.dto.PurchaserDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.hypretail.vcart.domain.Purchaser}.
 */
public interface PurchaserService {

    /**
     * Save a purchaser.
     *
     * @param purchaserDTO the entity to save.
     * @return the persisted entity.
     */
    PurchaserDTO save(PurchaserDTO purchaserDTO);

    /**
     * Get all the purchasers.
     *
     * @return the list of entities.
     */
    List<PurchaserDTO> findAll();


    /**
     * Get the "id" purchaser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchaserDTO> findOne(Long id);

    /**
     * Delete the "id" purchaser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
