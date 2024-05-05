package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BuyerRepository extends MongoRepository<Buyer, String> {
    Optional<Buyer> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<Buyer> findByActiveReservationsContains(String reservationId);
}
