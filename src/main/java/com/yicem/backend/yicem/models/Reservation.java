package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservations")
@Getter
@Setter
public class Reservation {
    @Id
    @NonNull
    private String id;

    private String buyerId;
    private String sellerId;
    private String offerId;

    private String timeSlot;

    public Reservation(String buyerId, String sellerId, String offerId, String timeSlot) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.offerId = offerId;
        this.timeSlot = timeSlot;
    }
}
