package com.example.ecommerce.service;

import com.example.ecommerce.dto.ReviewDto;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, ReviewDto updatedReview) {
        Review review = getReviewById(id);
        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
}