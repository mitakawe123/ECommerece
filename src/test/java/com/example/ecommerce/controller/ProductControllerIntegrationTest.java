package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void getAllProducts_ReturnsListOfProducts() throws Exception {
        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        Product product1 = new Product();
        product1.setName("Product 1a");
        product1.setDescription("Description 1a");
        product1.setPrice(BigDecimal.valueOf(10.99));
        product1.setStockQuantity(100);
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setName("Product 2a");
        product2.setDescription("Description 2a");
        product2.setPrice(BigDecimal.valueOf(20.99));
        product2.setStockQuantity(50);
        product2.setCategory(category);

        productRepository.saveAll(List.of(product1, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Product 1a")))
                .andExpect(jsonPath("$[1].name", is("Product 2a")));
    }

    @Test
    void getProductById_ReturnsProduct() throws Exception {
        var timestamp = System.currentTimeMillis();
        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Product 1-" + timestamp);
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.valueOf(10.99));
        product.setStockQuantity(100);
        product.setCategory(category);

        productRepository.save(product);

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Product 1-" + timestamp)));
    }

    @Test
    void createProduct_ReturnsCreated() throws Exception {
        var timestamp = System.currentTimeMillis();

        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        ProductDto productDto = new ProductDto(null);
        productDto.setName("Product 1" + timestamp);
        productDto.setDescription("Description 1");
        productDto.setPrice(BigDecimal.valueOf(10.99));
        productDto.setStockQuantity(100);
        productDto.setTagIds(Set.of());
        productDto.setCategoryId(category.getId());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Product 1" + timestamp)));
    }

    @Test
    void updateProduct_ReturnsUpdatedProduct() throws Exception {
        var timestamp = System.currentTimeMillis();

        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Product 1" + timestamp);
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.valueOf(10.99));
        product.setStockQuantity(100);
        product.setCategory(category);

        productRepository.save(product);

        product.setName("Updated Product");
        product.setDescription("Updated Description");

        mockMvc.perform(put("/api/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Product")));
    }

    @Test
    void deleteProduct_ReturnsNoContent() throws Exception {
        var timestamp = System.currentTimeMillis();

        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Product 1" + timestamp);
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.valueOf(10.99));
        product.setStockQuantity(100);
        product.setCategory(category);

        productRepository.save(product);

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProductById_NotFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }
}
