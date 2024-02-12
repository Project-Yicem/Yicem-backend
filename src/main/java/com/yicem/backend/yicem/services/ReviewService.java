package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.dtos.ReviewDTO;
import com.yicem.backend.yicem.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ReviewDTO save(ReviewDTO reviewDTO){
        return new ReviewDTO(reviewRepository.save(reviewDTO.toReview()));
    }

    public List<ReviewDTO> saveAll(List<ReviewDTO> reviews) {
        return reviews.stream()
                .map(ReviewDTO::toReview)
                .peek(reviewRepository::save)
                .map(ReviewDTO::new)
                .toList();
    }

    public List<ReviewDTO> findAll() {
        return reviewRepository.findAll().stream().map(ReviewDTO::new).toList();
    }

    public List<ReviewDTO> findAll(List<String> ids) {
        return reviewRepository.findAll(ids).stream().map(ReviewDTO::new).toList();
    }

    public ReviewDTO findOne(String id) {
        return new ReviewDTO(reviewRepository.findOne(id));
    }

    public long count() {
        return reviewRepository.count();
    }

    public long delete(String id) {
        return reviewRepository.delete(id);
    }

    public long delete(List<String> ids) {
        return reviewRepository.delete(ids);
    }

    public long deleteAll() {
        return reviewRepository.deleteAll();
    }

    public ReviewDTO update(ReviewDTO ReviewDTO) {
        return new ReviewDTO(reviewRepository.update(ReviewDTO.toReview()));
    }

    public long update(List<ReviewDTO> reviews) {
        return reviewRepository.update(reviews.stream().map(ReviewDTO::toReview).toList());
    }

}
