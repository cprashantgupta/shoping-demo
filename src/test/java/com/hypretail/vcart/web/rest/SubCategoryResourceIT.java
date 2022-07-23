package com.hypretail.vcart.web.rest;


import com.hypretail.vcart.domain.SubCategory;
import com.hypretail.vcart.repository.SubCategoryRepository;

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

import com.hypretail.vcart.service.dto.SubCategoryDTO;
import com.hypretail.vcart.service.mapper.SubCategoryMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for the {@Link SubCategoryResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubCategoryResourceIT {

    private static final String DEFAULT_SUB_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY_NAME = "BBBBBBBBBB";

    @Inject
    private SubCategoryMapper subCategoryMapper;
    @Inject
    private SubCategoryRepository subCategoryRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private SubCategory subCategory;

    @BeforeEach
    public void initTest() {
        subCategory = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        subCategoryRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createEntity() {
        SubCategory subCategory = new SubCategory()
            .subCategoryName(DEFAULT_SUB_CATEGORY_NAME);
        return subCategory;
    }


    @Test
    public void createSubCategory() throws Exception {
        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();

        // Create the SubCategory
        HttpResponse<SubCategory> response = client.exchange(HttpRequest.POST("/api/sub-categories", subCategory), SubCategory.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);

        assertThat(testSubCategory.getSubCategoryName()).isEqualTo(DEFAULT_SUB_CATEGORY_NAME);
    }

    @Test
    public void createSubCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();

        // Create the SubCategory with an existing ID
        subCategory.setId(1L);
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<SubCategory> response = client.exchange(HttpRequest.POST("/api/sub-categories", subCategory), SubCategory.class)
            .onErrorReturn(t -> (HttpResponse<SubCategory>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkSubCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCategoryRepository.findAll().size();
        // set the field null
        subCategory.setSubCategoryName(null);

        // Create the SubCategory, which fails.
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        HttpResponse<SubCategory> response = client.exchange(HttpRequest.POST("/api/sub-categories", subCategory), SubCategory.class)
            .onErrorReturn(t -> (HttpResponse<SubCategory>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSubCategories() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get the subCategoryList w/ all the subCategories
        List<SubCategory> subCategories = client.retrieve(HttpRequest.GET("/api/sub-categories?eagerload=true"), Argument.listOf(SubCategory.class)).blockingFirst();
        SubCategory testSubCategory = subCategories.get(0);


        assertThat(testSubCategory.getSubCategoryName()).isEqualTo(DEFAULT_SUB_CATEGORY_NAME);
    }

    @Test
    public void getSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get the subCategory
        SubCategory testSubCategory = client.retrieve(HttpRequest.GET("/api/sub-categories/" + this.subCategory.getId()), SubCategory.class).blockingFirst();


        assertThat(testSubCategory.getSubCategoryName()).isEqualTo(DEFAULT_SUB_CATEGORY_NAME);
    }

    @Test
    public void getNonExistingSubCategory() throws Exception {
        // Get the subCategory
        HttpResponse<SubCategory> response = client.exchange(HttpRequest.GET("/api/sub-categories/"+ Long.MAX_VALUE), SubCategory.class)
            .onErrorReturn(t -> (HttpResponse<SubCategory>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // Update the subCategory
        SubCategory updatedSubCategory = subCategoryRepository.findById(subCategory.getId()).get();

        updatedSubCategory
            .subCategoryName(UPDATED_SUB_CATEGORY_NAME);
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(updatedSubCategory);

        HttpResponse<SubCategory> response = client.exchange(HttpRequest.PUT("/api/sub-categories", updatedSubCategory), SubCategory.class)
            .onErrorReturn(t -> (HttpResponse<SubCategory>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);

        assertThat(testSubCategory.getSubCategoryName()).isEqualTo(UPDATED_SUB_CATEGORY_NAME);
    }

    @Test
    public void updateNonExistingSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        HttpResponse<SubCategory> response = client.exchange(HttpRequest.PUT("/api/sub-categories", subCategory), SubCategory.class)
            .onErrorReturn(t -> (HttpResponse<SubCategory>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSubCategory() throws Exception {
        // Initialize the database with one entity
        subCategoryRepository.saveAndFlush(subCategory);

        int databaseSizeBeforeDelete = subCategoryRepository.findAll().size();

        // Delete the subCategory
        HttpResponse<SubCategory> response = client.exchange(HttpRequest.DELETE("/api/sub-categories/"+ subCategory.getId()), SubCategory.class)
            .onErrorReturn(t -> (HttpResponse<SubCategory>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        // Validate the database is now empty
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCategory.class);
        SubCategory subCategory1 = new SubCategory();
        subCategory1.setId(1L);
        SubCategory subCategory2 = new SubCategory();
        subCategory2.setId(subCategory1.getId());
        assertThat(subCategory1).isEqualTo(subCategory2);
        subCategory2.setId(2L);
        assertThat(subCategory1).isNotEqualTo(subCategory2);
        subCategory1.setId(null);
        assertThat(subCategory1).isNotEqualTo(subCategory2);
    }
}
