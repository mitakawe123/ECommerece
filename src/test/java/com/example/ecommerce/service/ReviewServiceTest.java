package com.example.ecommerce.service;

import com.example.ecommerce.dto.ReviewDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReviews_ReturnsListOfReviews() {
        Review review1 = new Review();
        review1.setRating(5);
        review1.setComment("Excellent product!");
        Review review2 = new Review();
        review2.setRating(4);
        review2.setComment("Very good.");

        when(reviewRepository.findAll()).thenReturn(Arrays.asList(review1, review2));

        List<Review> reviews = reviewService.getAllReviews();

        assertNotNull(reviews);
        assertEquals(2, reviews.size());
        assertEquals(5, reviews.getFirst().getRating());
        assertEquals("Excellent product!", reviews.getFirst().getComment());
    }

    @Test
    void getReviewById_ValidId_ReturnsReview() {
        Long reviewId = 1L;
        Review review = new Review();
        review.setRating(5);
        review.setComment("Excellent product!");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Review foundReview = reviewService.getReviewById(reviewId);

        assertNotNull(foundReview);
        assertEquals(5, foundReview.getRating());
        assertEquals("Excellent product!", foundReview.getComment());
    }

    @Test
    void getReviewById_InvalidId_ThrowsResourceNotFoundException() {
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.getReviewById(reviewId);
        });
    }

    @Test
    void createReview_SavesAndReturnsReview() {
        Review review = new Review();
        review.setRating(5);
        review.setComment("Excellent product!");

        when(reviewRepository.save(review)).thenReturn(review);

        Review createdReview = reviewService.createReview(review);

        assertNotNull(createdReview);
        assertEquals(5, createdReview.getRating());
        assertEquals("Excellent product!", createdReview.getComment());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void updateReview_ValidId_UpdatesAndReturnsReview() {
        Long reviewId = 1L;
        Review existingReview = new Review();
        existingReview.setRating(3);
        existingReview.setComment("Average product.");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        ReviewDto updatedReview = new ReviewDto();
        updatedReview.setRating(4);
        updatedReview.setComment("Good product.");

        when(reviewRepository.save(existingReview)).thenReturn(existingReview);

        Review result = reviewService.updateReview(reviewId, updatedReview);

        assertNotNull(result);
        assertEquals(4, result.getRating());
        assertEquals("Good product.", result.getComment());

        verify(reviewRepository, times(1)).save(existingReview);
    }

    @Test
    void updateReview_InvalidId_ThrowsResourceNotFoundException() {
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        ReviewDto updatedReview = new ReviewDto();
        updatedReview.setRating(4);
        updatedReview.setComment("Good product.");

        assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.updateReview(reviewId, updatedReview);
        });
    }

    @Test
    void deleteReview_ValidId_DeletesReview() {
        Long reviewId = 1L;
        Review review = new Review();
        review.setRating(5);
        review.setComment("Excellent product!");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewService.deleteReview(reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }
}