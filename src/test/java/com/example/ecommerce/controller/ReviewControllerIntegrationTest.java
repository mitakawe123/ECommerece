package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ReviewRepository;
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

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@ActiveProfiles("test")
class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        customer = new Customer();
        customer.setFullName("John Doe");
        customer.setPhone("098098798798");
        customer.setPassword("johndoe");
        customer.setEmail("john.doe@example.com");
        customer.setUsername("johndoe");
        customer = customerRepository.save(customer);

        Category category = new Category();
        category.setDescription("Sample Description for Category");
        category.setName("Sample Category");
        categoryRepository.save(category);

        product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setDescription("Sample Description");
        product.setStockQuantity(5);
        product.setCategory(category);
        product = productRepository.save(product);
    }

    @Test
    void getAllReviews_ReturnsListOfReviews() throws Exception {
        Review review1 = new Review();
        review1.setComment("Great product!");
        review1.setRating(5);
        review1.setCustomer(customer);
        review1.setProduct(product);

        Review review2 = new Review();
        review2.setComment("Not bad");
        review2.setRating(3);
        review2.setCustomer(customer);
        review2.setProduct(product);

        reviewRepository.saveAll(List.of(review1, review2));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].comment", is("Great product!")))
                .andExpect(jsonPath("$[1].comment", is("Not bad")));
    }

    @Test
    void getReviewById_ReturnsReview() throws Exception {
        Review review = new Review();
        review.setComment("I really loved this product.");
        review.setRating(5);
        review.setCustomer(customer);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);

        mockMvc.perform(get("/api/reviews/{id}", savedReview.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment", is("I really loved this product.")));
    }

    @Test
    void createReview_ReturnsCreated() throws Exception {
        var customerId = customer.getId();
        var productId = product.getId();

        String newReviewJson = String.format("""
            {
                "comment": "This is the best product ever!",
                "rating": 5,
                "customerId": %d,
                "productId": %d
            }
            """, customerId, productId);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newReviewJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment", is("This is the best product ever!")));
    }

    @Test
    void updateReview_ReturnsUpdatedReview() throws Exception {
        Review review = new Review();
        review.setComment("I really loved this product.");
        review.setRating(5);
        review.setCustomer(customer);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);

        String updatedReviewJson = String.format("""
                {
                    "comment": "The product is now even better.",
                    "rating": 4,
                    "customerId": %d,
                    "productId": %d
                }
                """, customer.getId(), product.getId());

        mockMvc.perform(put("/api/reviews/{id}", savedReview.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedReviewJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment", is("The product is now even better.")));
    }

    @Test
    void deleteReview_ReturnsNoContent() throws Exception {
        Review review = new Review();
        review.setComment("This review will be deleted.");
        review.setRating(1);
        review.setCustomer(customer);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);

        mockMvc.perform(delete("/api/reviews/{id}", savedReview.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/reviews/{id}", savedReview.getId()))
                .andExpect(status().isNotFound());
    }
}
