package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {


}
