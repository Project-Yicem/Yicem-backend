package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
