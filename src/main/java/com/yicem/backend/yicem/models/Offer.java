package com.yicem.backend.yicem.models;

import com.yicem.backend.yicem.helpers.PickupTime;
import com.yicem.backend.yicem.payload.request.OfferRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "offers")
@Getter
@Setter
@NoArgsConstructor
public class Offer {
    @Id
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

    // Will have default (false, false, now, emptyList) values at initialization
    private boolean isCompleted;
    private Date offeredAt;
    private List<String> reservations;

    public Offer(OfferRequest request, String sellerId) {
        this.sellerId = sellerId;
        this.offerName = request.getOfferName();
        this.description = request.getDescription();
        this.price = request.getPrice();
        this.itemCount = request.getItemCount();
        this.isMysteryBox = request.isMysteryBox();
        this.categories = new ArrayList<>();
        if( request.getCategories() != null && !request.getCategories().isEmpty()) {
            this.categories = request.getCategories();
        } else {
            this.categories.add("noCategory");
        }
        this.pickupTimes = request.getPickupTimes();

        this.isCompleted = false;
        this.offeredAt = new Date();
        this.reservations = new ArrayList<>();
    }

    public Offer(String description, boolean isMysteryBox, float price,
     int itemCount, String offerName, List<String> categories, List<PickupTime> pickupTimes ){
        this.description = description;
        this.isMysteryBox = isMysteryBox;
        this.price = price;
        this.itemCount = itemCount;
        this.offerName = offerName;
        if (categories == null) {
            this.categories = new ArrayList<>();
            this.categories.add("noCategory");
        }
        else{
            if (categories.isEmpty()) {
                this.categories = categories;
                this.categories.add("noCategory");
            }
            else{
                this.categories = categories;
            }
        }
        this.pickupTimes = pickupTimes;

        // Set values for new offers
        this.isCompleted = false;
        this.offeredAt = new Date();
        this.reservations = new ArrayList<>();
    }

    public void updateInfo(OfferRequest request){
        // id, isReserved, isCompleted, offeredAt, and reservations are not updated
        this.offerName = request.getOfferName();
        this.description = request.getDescription();
        this.price = request.getPrice();
        this.itemCount = request.getItemCount();
        this.isMysteryBox = request.isMysteryBox();
        if (request.getCategories() != null) {
            this.categories = request.getCategories();
        }
        if (this.categories.isEmpty()) {
            this.categories.add("noCategory");
        }
        this.pickupTimes = request.getPickupTimes();

    }

    /**
     * Decrement the offer's available item count by 1 and update isCompleted
     * @return true if updated itemCount is valid, false otherwise
     */
    public boolean sellItem() {

        if(this.itemCount > 0) {
            this.itemCount--;
            this.isCompleted = itemCount == 0;
            return true;
        }
        else return false;
    }

    public void addReservation(String reservationId) {
        this.reservations.add(reservationId);
    }

    public boolean removeReservation(String reservationId) {
        return this.reservations.remove(reservationId);
    }
}
