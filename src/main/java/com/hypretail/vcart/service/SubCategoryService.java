package com.hypretail.vcart.service;

import com.hypretail.vcart.service.dto.SubCategoryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.hypretail.vcart.domain.SubCategory}.
 */
public interface SubCategoryService {

    /**
     * Save a subCategory.
     *
     * @param subCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    SubCategoryDTO save(SubCategoryDTO subCategoryDTO);

    /**
     * Get all the subCategories.
     *
     * @return the list of entities.
     */
    List<SubCategoryDTO> findAll();


    /**
     * Get the "id" subCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" subCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
