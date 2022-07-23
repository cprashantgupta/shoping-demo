package com.hypretail.vcart.web.rest;

import com.hypretail.vcart.service.VendorService;
import com.hypretail.vcart.web.rest.errors.BadRequestAlertException;
import com.hypretail.vcart.service.dto.VendorDTO;

import com.hypretail.vcart.util.HeaderUtil;
import com.hypretail.vcart.util.PaginationUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;




import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hypretail.vcart.domain.Vendor}.
 */
@Controller("/api")
public class VendorResource {

    private final Logger log = LoggerFactory.getLogger(VendorResource.class);

    private static final String ENTITY_NAME = "vendor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendorService vendorService;

    public VendorResource(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    /**
     * {@code POST  /vendors} : Create a new vendor.
     *
     * @param vendorDTO the vendorDTO to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new vendorDTO, or with status {@code 400 (Bad Request)} if the vendor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/vendors")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<VendorDTO> createVendor(@Body VendorDTO vendorDTO) throws URISyntaxException {
        log.debug("REST request to save Vendor : {}", vendorDTO);
        if (vendorDTO.getId() != null) {
            throw new BadRequestAlertException("A new vendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VendorDTO result = vendorService.save(vendorDTO);
        URI location = new URI("/api/vendors/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /vendors} : Updates an existing vendor.
     *
     * @param vendorDTO the vendorDTO to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated vendorDTO,
     * or with status {@code 400 (Bad Request)} if the vendorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vendorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/vendors")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<VendorDTO> updateVendor(@Body VendorDTO vendorDTO) throws URISyntaxException {
        log.debug("REST request to update Vendor : {}", vendorDTO);
        if (vendorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VendorDTO result = vendorService.save(vendorDTO);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, vendorDTO.getId().toString()));
    }

    /**
     * {@code GET  /vendors} : get all the vendors.
     *
     * @param pageable the pagination information.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of vendors in body.
     */
    @Get("/vendors")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<List<VendorDTO>> getAllVendors(HttpRequest request, Pageable pageable) {
        log.debug("REST request to get a page of Vendors");
        Page<VendorDTO> page = vendorService.findAll(pageable);
        return HttpResponse.ok(page.getContent()).headers(headers ->
            PaginationUtil.generatePaginationHttpHeaders(headers, UriBuilder.of(request.getPath()), page));
    }

    /**
     * {@code GET  /vendors/:id} : get the "id" vendor.
     *
     * @param id the id of the vendorDTO to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the vendorDTO, or with status {@code 404 (Not Found)}.
     */
    @Get("/vendors/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<VendorDTO> getVendor(@PathVariable Long id) {
        log.debug("REST request to get Vendor : {}", id);
        
        return vendorService.findOne(id);
    }

    /**
     * {@code DELETE  /vendors/:id} : delete the "id" vendor.
     *
     * @param id the id of the vendorDTO to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/vendors/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteVendor(@PathVariable Long id) {
        log.debug("REST request to delete Vendor : {}", id);
        vendorService.delete(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
