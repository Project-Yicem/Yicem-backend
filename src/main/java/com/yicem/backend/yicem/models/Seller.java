package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Document(collection = "sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Seller {
    @Id
    @NonNull
    private String id;

    @NonNull
    private String username;

    @NonNull
    private boolean isApproved;

    @NonNull
    private String address;

    @NonNull
    private String phone;

    @NonNull
    private String businessName;

    @NonNull
    private LocalTime openingHour;

    @NonNull
    private LocalTime closingHour;

    @NonNull
    private String locationCoordinates;

    @NonNull
    private float reservationTimeout;

    private List<Review> reviews;

    private List<String> currentOffers;

    private List<String> pastTransactions;

}


