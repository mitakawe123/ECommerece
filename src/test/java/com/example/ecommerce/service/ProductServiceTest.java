package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_ReturnsListOfProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(BigDecimal.valueOf(100));
        product1.setStockQuantity(10);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(BigDecimal.valueOf(150));
        product2.setStockQuantity(5);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
        assertEquals("Product 2", products.get(1).getName());
    }

    @Test
    void getProductById_ValidId_ReturnsProduct() {
        Long productId = 70L;
        Product product = new Product();
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStockQuantity(10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(productId);

        assertEquals("Product 1", foundProduct.getName());
    }

    @Test
    void getProductById_InvalidId_ReturnsEmpty() {
        Long productId = 70L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(productId);
        });
    }

    @Test
    void createProduct_SavesAndReturnsProduct() {
        Product product = new Product();
        product.setName("New Product");
        product.setDescription("New Description");
        product.setPrice(BigDecimal.valueOf(200));
        product.setStockQuantity(20);

        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct);
        assertEquals("New Product", createdProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_ValidId_UpdatesAndReturnsProduct() {
        Long productId = 70L;
        Product existingProduct = new Product();
        existingProduct.setName("Old Product");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(BigDecimal.valueOf(50));
        existingProduct.setStockQuantity(5);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(BigDecimal.valueOf(75.0));
        updatedProduct.setStockQuantity(10);

        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = productService.updateProduct(productId, updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(75.0), result.getPrice());
        assertEquals(10, result.getStockQuantity());

        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProduct_InvalidId_ThrowsRuntimeException() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(productId, updatedProduct);
        });
    }

    @Test
    void deleteProduct_ValidId_DeletesProduct() {
        Long productId = 1L;

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}