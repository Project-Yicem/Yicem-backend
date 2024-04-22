package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import java.util.ArrayList;

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
    private String locationLatitude;
    private String locationLongitude;
    private float reservationTimeout;
    private float rating;

    //These will hold IDs instead of objects. They will initialized as Empty Lists.
    private ArrayList<String> reviews;
    private ArrayList<String> offers;
    private ArrayList<String> pastTransactions;

    public Seller(String id, String username, boolean isApproved, String address, String phone, String businessName,
                  String openingHour, String closingHour, String locationLatitude, String locationLongitude,
                  float reservationTimeout) {
        this.id = id;
        this.username = username;
        this.isApproved = isApproved;
        this.address = address;
        this.phone = phone;
        this.businessName = businessName;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.reservationTimeout = reservationTimeout;
        this.rating = 0;

        // They will initialize as empty lists
        this.reviews = new ArrayList<>();
        this.offers = new ArrayList<>();
        this.pastTransactions = new ArrayList<>();
    }

    public void addReview(String id, int rating) {
        // Check if review exists
        if(!reviews.contains(id)) {
            // Compute new rating score
            int reviewCount = reviews.size();
            float sum = this.rating * reviewCount + rating;
            this.rating = sum / (reviewCount + 1);

            // Add review to reviewList
            this.reviews.add(id);
        }
    }

    public void addOffer(String id) {
        if(!offers.contains(id)) {
            this.offers.add(id);
        }
    }

    public void removeOffer(String id) {
        this.offers.remove(id);
    }

    public void addTransaction(String id) {
        if(!pastTransactions.contains(id)) {
            this.pastTransactions.add(id);
        }
    }

}