package com.yicem.backend.yicem.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
public class Seller extends User {

    private String address;
    private String phone;
    private boolean isApproved;
    //private List<Review> reviews;
    private String businessName;
    //private List<Offer> currentOffers;
    private float reservationTimeout;
    private String workingHours;
    //private List<Transaction> pastTransactions;
    private String locationCoordinates;

    @Builder
    public Seller (ObjectId id, String username, String email, String password, String address, String phone, boolean isApproved, String businessName, float reservationTimeout,
                   String workingHours, String locationCoordinates) {
        super(id, username, email, password);
        this.address = address;
        this.phone = phone;
        this.isApproved = isApproved;
        this.businessName = businessName;
        this.reservationTimeout = reservationTimeout;
        this.workingHours = workingHours;
        this.locationCoordinates = locationCoordinates;
    }

}