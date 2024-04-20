package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "sellers")
@Getter
@Setter
public class Seller {
    @Id
    private String id;

    private String username;

    private boolean isApproved;

    private String address;

    private String phone;

    private String businessName;

    private String openingHour;

    private String closingHour;

    private String locationCoordinates;

    private float reservationTimeout;

    private List<String> reviews;

    private List<String> currentOffers;

    private List<String> pastTransactions;

    public Seller(String id, String username, boolean isApproved, String address, String phone, String businessName,
                  String openingHour, String closingHour, String locationCoordinates, float reservationTimeout) {
        this.id = id;
        this.username = username;
        this.isApproved = isApproved;
        this.address = address;
        this.phone = phone;
        this.businessName = businessName;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.locationCoordinates = locationCoordinates;
        this.reservationTimeout = reservationTimeout;
        this.reviews = new ArrayList<>();
        this.currentOffers = new ArrayList<>();
    }

    public void addReview(String review) {
        this.reviews.add(review);
    }
}