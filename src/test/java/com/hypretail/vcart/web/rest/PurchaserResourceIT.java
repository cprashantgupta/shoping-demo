package com.hypretail.vcart.web.rest;


import com.hypretail.vcart.domain.Purchaser;
import com.hypretail.vcart.repository.PurchaserRepository;

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

import com.hypretail.vcart.service.dto.PurchaserDTO;
import com.hypretail.vcart.service.mapper.PurchaserMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for the {@Link PurchaserResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PurchaserResourceIT {

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
    private PurchaserMapper purchaserMapper;
    @Inject
    private PurchaserRepository purchaserRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Purchaser purchaser;

    @BeforeEach
    public void initTest() {
        purchaser = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        purchaserRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Purchaser createEntity() {
        Purchaser purchaser = new Purchaser()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .emailId(DEFAULT_EMAIL_ID)
            .address(DEFAULT_ADDRESS)
            .gstNumber(DEFAULT_GST_NUMBER);
        return purchaser;
    }


    @Test
    public void createPurchaser() throws Exception {
        int databaseSizeBeforeCreate = purchaserRepository.findAll().size();

        // Create the Purchaser
        HttpResponse<Purchaser> response = client.exchange(HttpRequest.POST("/api/purchasers", purchaser), Purchaser.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Purchaser in the database
        List<Purchaser> purchaserList = purchaserRepository.findAll();
        assertThat(purchaserList).hasSize(databaseSizeBeforeCreate + 1);
        Purchaser testPurchaser = purchaserList.get(purchaserList.size() - 1);

        assertThat(testPurchaser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPurchaser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPurchaser.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testPurchaser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPurchaser.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
    }

    @Test
    public void createPurchaserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaserRepository.findAll().size();

        // Create the Purchaser with an existing ID
        purchaser.setId(1L);
        PurchaserDTO purchaserDTO = purchaserMapper.toDto(purchaser);

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<Purchaser> response = client.exchange(HttpRequest.POST("/api/purchasers", purchaser), Purchaser.class)
            .onErrorReturn(t -> (HttpResponse<Purchaser>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Purchaser in the database
        List<Purchaser> purchaserList = purchaserRepository.findAll();
        assertThat(purchaserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaserRepository.findAll().size();
        // set the field null
        purchaser.setName(null);

        // Create the Purchaser, which fails.
        PurchaserDTO purchaserDTO = purchaserMapper.toDto(purchaser);

        HttpResponse<Purchaser> response = client.exchange(HttpRequest.POST("/api/purchasers", purchaser), Purchaser.class)
            .onErrorReturn(t -> (HttpResponse<Purchaser>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Purchaser> purchaserList = purchaserRepository.findAll();
        assertThat(purchaserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaserRepository.findAll().size();
        // set the field null
        purchaser.setPhone(null);

        // Create the Purchaser, which fails.
        PurchaserDTO purchaserDTO = purchaserMapper.toDto(purchaser);

        HttpResponse<Purchaser> response = client.exchange(HttpRequest.POST("/api/purchasers", purchaser), Purchaser.class)
            .onErrorReturn(t -> (HttpResponse<Purchaser>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Purchaser> purchaserList = purchaserRepository.findAll();
        assertThat(purchaserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPurchasers() throws Exception {
        // Initialize the database
        purchaserRepository.saveAndFlush(purchaser);

        // Get the purchaserList w/ all the purchasers
        List<Purchaser> purchasers = client.retrieve(HttpRequest.GET("/api/purchasers?eagerload=true"), Argument.listOf(Purchaser.class)).blockingFirst();
        Purchaser testPurchaser = purchasers.get(0);


        assertThat(testPurchaser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPurchaser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPurchaser.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testPurchaser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPurchaser.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
    }

    @Test
    public void getPurchaser() throws Exception {
        // Initialize the database
        purchaserRepository.saveAndFlush(purchaser);

        // Get the purchaser
        Purchaser testPurchaser = client.retrieve(HttpRequest.GET("/api/purchasers/" + this.purchaser.getId()), Purchaser.class).blockingFirst();


        assertThat(testPurchaser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPurchaser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPurchaser.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testPurchaser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPurchaser.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
    }

    @Test
    public void getNonExistingPurchaser() throws Exception {
        // Get the purchaser
        HttpResponse<Purchaser> response = client.exchange(HttpRequest.GET("/api/purchasers/"+ Long.MAX_VALUE), Purchaser.class)
            .onErrorReturn(t -> (HttpResponse<Purchaser>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updatePurchaser() throws Exception {
        // Initialize the database
        purchaserRepository.saveAndFlush(purchaser);

        int databaseSizeBeforeUpdate = purchaserRepository.findAll().size();

        // Update the purchaser
        Purchaser updatedPurchaser = purchaserRepository.findById(purchaser.getId()).get();

        updatedPurchaser
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .emailId(UPDATED_EMAIL_ID)
            .address(UPDATED_ADDRESS)
            .gstNumber(UPDATED_GST_NUMBER);
        PurchaserDTO purchaserDTO = purchaserMapper.toDto(updatedPurchaser);

        HttpResponse<Purchaser> response = client.exchange(HttpRequest.PUT("/api/purchasers", updatedPurchaser), Purchaser.class)
            .onErrorReturn(t -> (HttpResponse<Purchaser>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Purchaser in the database
        List<Purchaser> purchaserList = purchaserRepository.findAll();
        assertThat(purchaserList).hasSize(databaseSizeBeforeUpdate);
        Purchaser testPurchaser = purchaserList.get(purchaserList.size() - 1);

        assertThat(testPurchaser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPurchaser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPurchaser.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testPurchaser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPurchaser.getGstNumber()).isEqualTo(UPDATED_GST_NUMBER);
    }

    @Test
    public void updateNonExistingPurchaser() throws Exception {
        int databaseSizeBeforeUpdate = purchaserRepository.findAll().size();

        // Create the Purchaser
        PurchaserDTO purchaserDTO = purchaserMapper.toDto(purchaser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        HttpResponse<Purchaser> response = client.exchange(HttpRequest.PUT("/api/purchasers", purchaser), Purchaser.class)
            .onErrorReturn(t -> (HttpResponse<Purchaser>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Purchaser in the database
        List<Purchaser> purchaserList = purchaserRepository.findAll();
        assertThat(purchaserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePurchaser() throws Exception {
        // Initialize the database with one entity
        purchaserRepository.saveAndFlush(purchaser);

        int databaseSizeBeforeDelete = purchaserRepository.findAll().size();

        // Delete the purchaser
        HttpResponse<Purchaser> response = client.exchange(HttpRequest.DELETE("/api/purchasers/"+ purchaser.getId()), Purchaser.class)
            .onErrorReturn(t -> (HttpResponse<Purchaser>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        // Validate the database is now empty
        List<Purchaser> purchaserList = purchaserRepository.findAll();
        assertThat(purchaserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Purchaser.class);
        Purchaser purchaser1 = new Purchaser();
        purchaser1.setId(1L);
        Purchaser purchaser2 = new Purchaser();
        purchaser2.setId(purchaser1.getId());
        assertThat(purchaser1).isEqualTo(purchaser2);
        purchaser2.setId(2L);
        assertThat(purchaser1).isNotEqualTo(purchaser2);
        purchaser1.setId(null);
        assertThat(purchaser1).isNotEqualTo(purchaser2);
    }
}
