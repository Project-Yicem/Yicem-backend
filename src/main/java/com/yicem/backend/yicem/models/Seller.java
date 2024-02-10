package com.yicem.backend.yicem.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

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

}