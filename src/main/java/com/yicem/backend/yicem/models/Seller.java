package com.yicem.backend.yicem.models;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Seller extends User {

    private boolean isApproved;
    private String address;
    private String phone;
    private String businessName;
    private String workingHours;
    private String locationCoordinates;
    private float reservationTimeout;
    private List<ObjectId> reviews;
    private List<ObjectId> currentOffers;
    private List<ObjectId> pastTransactions;

    @Builder
    public Seller (ObjectId id, String username, String email, String password, boolean isApproved, String address,
                   String phone, String businessName, String workingHours, String locationCoordinates,
                   float reservationTimeout, List<ObjectId> reviews, List<ObjectId> currentOffers,
                   List<ObjectId> pastTransactions) {
        super(id, username, email, password);
        this.isApproved = isApproved;
        this.address = address;
        this.phone = phone;
        this.businessName = businessName;
        this.workingHours = workingHours;
        this.locationCoordinates = locationCoordinates;
        this.reservationTimeout = reservationTimeout;
        this.reviews = reviews;
        this.currentOffers = currentOffers;
        this.pastTransactions = pastTransactions;
    }

}