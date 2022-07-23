package com.hypretail.vcart.service.impl;

import com.hypretail.vcart.service.PurchaserService;
import com.hypretail.vcart.domain.Purchaser;
import com.hypretail.vcart.repository.PurchaserRepository;
import com.hypretail.vcart.service.dto.PurchaserDTO;
import com.hypretail.vcart.service.mapper.PurchaserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import io.micronaut.transaction.annotation.ReadOnly;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Purchaser}.
 */
@Singleton
@Transactional
public class PurchaserServiceImpl implements PurchaserService {

    private final Logger log = LoggerFactory.getLogger(PurchaserServiceImpl.class);

    private final PurchaserRepository purchaserRepository;

    private final PurchaserMapper purchaserMapper;

    public PurchaserServiceImpl(PurchaserRepository purchaserRepository, PurchaserMapper purchaserMapper) {
        this.purchaserRepository = purchaserRepository;
        this.purchaserMapper = purchaserMapper;
    }

    /**
     * Save a purchaser.
     *
     * @param purchaserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PurchaserDTO save(PurchaserDTO purchaserDTO) {
        log.debug("Request to save Purchaser : {}", purchaserDTO);
        Purchaser purchaser = purchaserMapper.toEntity(purchaserDTO);
        purchaser = purchaserRepository.mergeAndSave(purchaser);
        return purchaserMapper.toDto(purchaser);
    }

    /**
     * Get all the purchasers.
     *
     * @return the list of entities.
     */
    @Override
    @ReadOnly
    @Transactional
    public List<PurchaserDTO> findAll() {
        log.debug("Request to get all Purchasers");
        return purchaserRepository.findAll().stream()
            .map(purchaserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one purchaser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @ReadOnly
    @Transactional
    public Optional<PurchaserDTO> findOne(Long id) {
        log.debug("Request to get Purchaser : {}", id);
        return purchaserRepository.findById(id)
            .map(purchaserMapper::toDto);
    }

    /**
     * Delete the purchaser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Purchaser : {}", id);
        purchaserRepository.deleteById(id);
    }
}
