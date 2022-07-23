package com.hypretail.vcart.web.rest;


import com.hypretail.vcart.domain.Vendor;
import com.hypretail.vcart.repository.VendorRepository;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;

import java.util.List;

import com.hypretail.vcart.service.dto.VendorDTO;
import com.hypretail.vcart.service.mapper.VendorMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for the {@Link VendorResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VendorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE = 1L;
    private static final Long UPDATED_PHONE = 2L;

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Long DEFAULT_GST_NUMBER = 1L;
    private static final Long UPDATED_GST_NUMBER = 2L;

    @Inject
    private VendorMapper vendorMapper;
    @Inject
    private VendorRepository vendorRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Vendor vendor;

    @BeforeEach
    public void initTest() {
        vendor = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        vendorRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendor createEntity() {
        Vendor vendor = new Vendor()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .emailId(DEFAULT_EMAIL_ID)
            .address(DEFAULT_ADDRESS)
            .gstNumber(DEFAULT_GST_NUMBER);
        return vendor;
    }


    @Test
    public void createVendor() throws Exception {
        int databaseSizeBeforeCreate = vendorRepository.findAll().size();

        // Create the Vendor
        HttpResponse<Vendor> response = client.exchange(HttpRequest.POST("/api/vendors", vendor), Vendor.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);

        assertThat(testVendor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVendor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testVendor.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testVendor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testVendor.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
    }

    @Test
    public void createVendorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vendorRepository.findAll().size();

        // Create the Vendor with an existing ID
        vendor.setId(1L);
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<Vendor> response = client.exchange(HttpRequest.POST("/api/vendors", vendor), Vendor.class)
            .onErrorReturn(t -> (HttpResponse<Vendor>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendorRepository.findAll().size();
        // set the field null
        vendor.setName(null);

        // Create the Vendor, which fails.
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        HttpResponse<Vendor> response = client.exchange(HttpRequest.POST("/api/vendors", vendor), Vendor.class)
            .onErrorReturn(t -> (HttpResponse<Vendor>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendorRepository.findAll().size();
        // set the field null
        vendor.setPhone(null);

        // Create the Vendor, which fails.
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        HttpResponse<Vendor> response = client.exchange(HttpRequest.POST("/api/vendors", vendor), Vendor.class)
            .onErrorReturn(t -> (HttpResponse<Vendor>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllVendors() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get the vendorList w/ all the vendors
        List<Vendor> vendors = client.retrieve(HttpRequest.GET("/api/vendors?eagerload=true"), Argument.listOf(Vendor.class)).blockingFirst();
        Vendor testVendor = vendors.get(0);


        assertThat(testVendor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVendor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testVendor.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testVendor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testVendor.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
    }

    @Test
    public void getVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get the vendor
        Vendor testVendor = client.retrieve(HttpRequest.GET("/api/vendors/" + this.vendor.getId()), Vendor.class).blockingFirst();


        assertThat(testVendor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVendor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testVendor.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testVendor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testVendor.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
    }

    @Test
    public void getNonExistingVendor() throws Exception {
        // Get the vendor
        HttpResponse<Vendor> response = client.exchange(HttpRequest.GET("/api/vendors/"+ Long.MAX_VALUE), Vendor.class)
            .onErrorReturn(t -> (HttpResponse<Vendor>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor
        Vendor updatedVendor = vendorRepository.findById(vendor.getId()).get();

        updatedVendor
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .emailId(UPDATED_EMAIL_ID)
            .address(UPDATED_ADDRESS)
            .gstNumber(UPDATED_GST_NUMBER);
        VendorDTO vendorDTO = vendorMapper.toDto(updatedVendor);

        HttpResponse<Vendor> response = client.exchange(HttpRequest.PUT("/api/vendors", updatedVendor), Vendor.class)
            .onErrorReturn(t -> (HttpResponse<Vendor>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendorList.get(vendorList.size() - 1);

        assertThat(testVendor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVendor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testVendor.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testVendor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testVendor.getGstNumber()).isEqualTo(UPDATED_GST_NUMBER);
    }

    @Test
    public void updateNonExistingVendor() throws Exception {
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        HttpResponse<Vendor> response = client.exchange(HttpRequest.PUT("/api/vendors", vendor), Vendor.class)
            .onErrorReturn(t -> (HttpResponse<Vendor>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Vendor in the database
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteVendor() throws Exception {
        // Initialize the database with one entity
        vendorRepository.saveAndFlush(vendor);

        int databaseSizeBeforeDelete = vendorRepository.findAll().size();

        // Delete the vendor
        HttpResponse<Vendor> response = client.exchange(HttpRequest.DELETE("/api/vendors/"+ vendor.getId()), Vendor.class)
            .onErrorReturn(t -> (HttpResponse<Vendor>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        // Validate the database is now empty
        List<Vendor> vendorList = vendorRepository.findAll();
        assertThat(vendorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendor.class);
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        Vendor vendor2 = new Vendor();
        vendor2.setId(vendor1.getId());
        assertThat(vendor1).isEqualTo(vendor2);
        vendor2.setId(2L);
        assertThat(vendor1).isNotEqualTo(vendor2);
        vendor1.setId(null);
        assertThat(vendor1).isNotEqualTo(vendor2);
    }
}
