package com.hypretail.vcart.web.rest;

import com.hypretail.vcart.service.PurchaserService;
import com.hypretail.vcart.web.rest.errors.BadRequestAlertException;
import com.hypretail.vcart.service.dto.PurchaserDTO;

import com.hypretail.vcart.util.HeaderUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hypretail.vcart.domain.Purchaser}.
 */
@Controller("/api")
public class PurchaserResource {

    private final Logger log = LoggerFactory.getLogger(PurchaserResource.class);

    private static final String ENTITY_NAME = "purchaser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaserService purchaserService;

    public PurchaserResource(PurchaserService purchaserService) {
        this.purchaserService = purchaserService;
    }

    /**
     * {@code POST  /purchasers} : Create a new purchaser.
     *
     * @param purchaserDTO the purchaserDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new purchaserDTO, or with status {@code 400 (Bad Request)} if the purchaser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/purchasers")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<PurchaserDTO> createPurchaser(@Body PurchaserDTO purchaserDTO) throws URISyntaxException {
        log.debug("REST request to save Purchaser : {}", purchaserDTO);
        if (purchaserDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaserDTO result = purchaserService.save(purchaserDTO);
        URI location = new URI("/api/purchasers/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /purchasers} : Updates an existing purchaser.
     *
     * @param purchaserDTO the purchaserDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated purchaserDTO,
     * or with status {@code 400 (Bad Request)} if the purchaserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/purchasers")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<PurchaserDTO> updatePurchaser(@Body PurchaserDTO purchaserDTO) throws URISyntaxException {
        log.debug("REST request to update Purchaser : {}", purchaserDTO);
        if (purchaserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaserDTO result = purchaserService.save(purchaserDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, purchaserDTO.getId().toString()));
    }

    /**
     * {@code GET  /purchasers} : get all the purchasers.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of purchasers in body.
     */
    @Get("/purchasers")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<PurchaserDTO> getAllPurchasers(HttpRequest request) {
        log.debug("REST request to get all Purchasers");
        return purchaserService.findAll();
    }

    /**
     * {@code GET  /purchasers/:id} : get the "id" purchaser.
     *
     * @param id the id of the purchaserDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the purchaserDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/purchasers/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<PurchaserDTO> getPurchaser(@PathVariable Long id) {
        log.debug("REST request to get Purchaser : {}", id);
        
        return purchaserService.findOne(id);
    }

    /**
     * {@code DELETE  /purchasers/:id} : delete the "id" purchaser.
     *
     * @param id the id of the purchaserDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/purchasers/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deletePurchaser(@PathVariable Long id) {
        log.debug("REST request to delete Purchaser : {}", id);
        purchaserService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
