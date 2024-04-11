package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Seller;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends MongoRepository<Seller, String> {
    Optional<Seller> findByUsername(String username);

    Boolean existsByUsername(String username);

    List<Seller> findByIsApproved(boolean isApproved);
}
