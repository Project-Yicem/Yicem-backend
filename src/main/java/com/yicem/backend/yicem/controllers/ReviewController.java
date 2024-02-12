package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.dtos.ReviewDTO;
import com.yicem.backend.yicem.services.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("review")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDTO postReview(@RequestBody ReviewDTO reviewDTO){
        return reviewService.save(reviewDTO);
    }

    @PostMapping("reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ReviewDTO> postReviews(@RequestBody List<ReviewDTO> reviews) {
        return reviewService.saveAll(reviews);
    }

    @GetMapping("reviews")
    public List<ReviewDTO> getReviews() {
        return reviewService.findAll();
    }

    @GetMapping("review/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable String id) {
        ReviewDTO ReviewDTO = reviewService.findOne(id);
        if (ReviewDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(ReviewDTO);
    }

    @GetMapping("reviews/{ids}")
    public List<ReviewDTO> getReviews(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return reviewService.findAll(listIds);
    }

    @GetMapping("reviews/count")
    public Long getCount() {
        return reviewService.count();
    }

    @DeleteMapping("review/{id}")
    public Long deleteReview(@PathVariable String id) {
        return reviewService.delete(id);
    }

    @DeleteMapping("reviews/{ids}")
    public Long deleteReviews(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return reviewService.delete(listIds);
    }

    @DeleteMapping("reviews")
    public Long deleteReviews() {
        return reviewService.deleteAll();
    }

    @PutMapping("review")
    public ReviewDTO putReview(@RequestBody ReviewDTO ReviewDTO) {
        return reviewService.update(ReviewDTO);
    }

    @PutMapping("reviews")
    public Long putReview(@RequestBody List<ReviewDTO> reviews) {
        return reviewService.update(reviews);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }
}

