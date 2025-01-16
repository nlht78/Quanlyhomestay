package com.homestay.management.service;




import com.homestay.management.model.Review;
import com.homestay.management.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviewsByRoomId(Long roomId) {
        return reviewRepository.findByRoomId(roomId);
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }
}
