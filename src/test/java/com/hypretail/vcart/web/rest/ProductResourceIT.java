package com.hypretail.vcart.web.rest;


import com.hypretail.vcart.domain.Product;
import com.hypretail.vcart.repository.ProductRepository;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;

import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;

import com.hypretail.vcart.service.dto.ProductDTO;
import com.hypretail.vcart.service.mapper.ProductMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration tests for the {@Link ProductResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductResourceIT {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    @Inject
    private ProductMapper productMapper;
    @Inject
    private ProductRepository productRepository;

    @Inject @Client("/")
    RxHttpClient client;

    private Product product;

    @BeforeEach
    public void initTest() {
        product = createEntity();
    }

    @AfterEach
    public void cleanUpTest() {
        productRepository.deleteAll();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        Product product = new Product()
            .productName(DEFAULT_PRODUCT_NAME);
        return product;
    }


    @Test
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        HttpResponse<Product> response = client.exchange(HttpRequest.POST("/api/products", product), Product.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);

        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
    }

    @Test
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<Product> response = client.exchange(HttpRequest.POST("/api/products", product), Product.class)
            .onErrorReturn(t -> (HttpResponse<Product>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        HttpResponse<Product> response = client.exchange(HttpRequest.POST("/api/products", product), Product.class)
            .onErrorReturn(t -> (HttpResponse<Product>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the productList w/ all the products
        List<Product> products = client.retrieve(HttpRequest.GET("/api/products?eagerload=true"), Argument.listOf(Product.class)).blockingFirst();
        Product testProduct = products.get(0);


        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
    }

    @Test
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        Product testProduct = client.retrieve(HttpRequest.GET("/api/products/" + this.product.getId()), Product.class).blockingFirst();


        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
    }

    @Test
    public void getNonExistingProduct() throws Exception {
        // Get the product
        HttpResponse<Product> response = client.exchange(HttpRequest.GET("/api/products/"+ Long.MAX_VALUE), Product.class)
            .onErrorReturn(t -> (HttpResponse<Product>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    @Disabled
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();

        updatedProduct
            .productName(UPDATED_PRODUCT_NAME);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        HttpResponse<Product> response = client.exchange(HttpRequest.PUT("/api/products", updatedProduct), Product.class)
            .onErrorReturn(t -> (HttpResponse<Product>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);

        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
    }

    @Test
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        HttpResponse<Product> response = client.exchange(HttpRequest.PUT("/api/products", product), Product.class)
            .onErrorReturn(t -> (HttpResponse<Product>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProduct() throws Exception {
        // Initialize the database with one entity
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        HttpResponse<Product> response = client.exchange(HttpRequest.DELETE("/api/products/"+ product.getId()), Product.class)
            .onErrorReturn(t -> (HttpResponse<Product>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        // Validate the database is now empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }
}
