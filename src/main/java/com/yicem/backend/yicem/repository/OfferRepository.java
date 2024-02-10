package com.yicem.backend.yicem.repository;

import com.yicem.backend.yicem.entity.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {

}
