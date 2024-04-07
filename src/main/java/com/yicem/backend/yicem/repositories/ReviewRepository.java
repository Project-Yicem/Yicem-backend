package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {

}
