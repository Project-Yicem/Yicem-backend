package com.yicem.backend.yicem.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yicem.backend.yicem.helpers.PickupTime;
import com.yicem.backend.yicem.models.Offer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferResponse {
    private String id;

    // Will take their values from request
    private String sellerId;
    private String offerName;
    private String description;
    private float price;
    private int itemCount;
    private boolean isMysteryBox;
    private List<String> categories;
    private List<PickupTime> pickupTimes;

    private boolean isCompleted;
    private Date offeredAt;
    private List<ReservationResponse> reservations;

    public OfferResponse(Offer offer) {
        this.id = offer.getId();
        this.sellerId = offer.getSellerId();
        this.offerName = offer.getOfferName();
        this.description = offer.getDescription();
        this.price = offer.getPrice();
        this.itemCount = offer.getItemCount();
        this.isMysteryBox = offer.isMysteryBox();
        this.categories = offer.getCategories();
        this.pickupTimes = offer.getPickupTimes();
        this.isCompleted = offer.isCompleted();
        this.offeredAt = offer.getOfferedAt();
        this.reservations = new ArrayList<>();
    }
}
