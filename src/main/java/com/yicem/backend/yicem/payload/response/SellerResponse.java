package com.yicem.backend.yicem.payload.response;

import com.yicem.backend.yicem.models.Seller;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SellerResponse {
    // Common attributes
    private String id;
    private String username;

    //Seller attributes
    private boolean isApproved;
    @Size(max = 100)
    private String address;
    @Size(max = 10)
    private String phone;
    @Size(max = 20)
    private String businessName;
    private String openingHour;
    private String closingHour;
    private String locationLatitude;
    private String locationLongitude;
    private String logo;
    private float reservationTimeout;
    private List<String> reviews;
    private List<String> offers;
    private List<String> pastTransactions;

    //Additional Attributes
    private boolean isOpen;
    private float rating;

    public SellerResponse(Seller seller) {
        this.id = seller.getId();
        this.username = seller.getUsername();
        this.isApproved = seller.isApproved();
        this.address = seller.getAddress();
        this.phone = seller.getPhone();
        this.businessName = seller.getBusinessName();
        this.openingHour = seller.getOpeningHour();
        this.closingHour = seller.getClosingHour();
        this.locationLatitude = seller.getLocationLatitude();
        this.locationLongitude = seller.getLocationLongitude();
        this.logo = seller.getLogo();
        this.reservationTimeout = seller.getReservationTimeout();
        this.reviews = seller.getReviews();
        this.offers = seller.getOffers();
        this.pastTransactions = seller.getPastTransactions();
    }
}


