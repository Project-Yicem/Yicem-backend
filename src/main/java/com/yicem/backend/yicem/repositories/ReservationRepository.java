package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    boolean existsByBuyerIdAndOfferId(String buyerId, String offerId);

    List<Reservation> findAllByOfferId(String buyerId);

}
