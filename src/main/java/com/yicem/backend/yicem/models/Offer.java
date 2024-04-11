package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Offer {
    @Id
    @NonNull
    private String id;

    @NonNull
    private String description;

    @NonNull
    private boolean isMysteryBox;

    @NonNull
    private float price;

    @NonNull
    private int itemCount;

    @NonNull
    private String offerName;


    private List<String> categories;

    @NonNull
    private boolean isReserved;

    @NonNull
    private boolean isCompleted;

    @NonNull
    private Date offeredAt;

    @NonNull
    @DBRef
    private List<Reservation> reservations;

    public Offer(Offer newOffer){
        this.description = newOffer.description;
        this.isMysteryBox = newOffer.isMysteryBox;
        this.price = newOffer.price;
        this.itemCount = newOffer.itemCount;
        this.offerName = newOffer.offerName;
        if (newOffer.categories == null) {
            this.categories = new ArrayList<String>();
            this.categories.add("noCategory");
        }
        else{
            if (newOffer.categories.isEmpty()) {
                this.categories = newOffer.categories;
                this.categories.add("noCategory");
            }
            else{
                this.categories = newOffer.categories;
            }
        }
        // set values for new offers
        this.isReserved = false;
        this.isCompleted = false;
        this.offeredAt = new Date();
        this.reservations = new ArrayList<Reservation>();
    }

    public Offer(String description, boolean isMysteryBox, float price,
     int itemCount, String offerName, List<String> categories ){
        this.description = description;
        this.isMysteryBox = isMysteryBox;
        this.price = price;
        this.itemCount = itemCount;
        this.offerName = offerName;
        if (categories == null) {
            this.categories = new ArrayList<String>();
            this.categories.add("noCateegory");
        }
        else{
            if (categories.isEmpty()) {
                this.categories = categories;
                this.categories.add("noCateegory");
            }
            else{
                this.categories = categories;
            }
        }
        // set values for new offers
        this.isReserved = false;
        this.isCompleted = false;
        this.offeredAt = new Date();
        this.reservations = new ArrayList<Reservation>();
    }

    public void updateInfo(Offer newOffer){
        // id, isReserved, isCompleted, offeredAt, and reservations are not updated
        this.description = newOffer.description;
        this.isMysteryBox = newOffer.isMysteryBox;
        this.price = newOffer.price;
        this.itemCount = newOffer.itemCount;
        this.offerName = newOffer.offerName;
        this.categories = newOffer.categories;
    }

    /**
     * Decrement the offer's available item count by 1 and update isCompleted
     * @return true if updated itemCount is valid, false otherwise
     */
    public boolean decrementItemCount(){
        this.itemCount -= 1;
        if (itemCount == 0) {
            this.isCompleted = true;
            return true;
        }
        else if(itemCount > 0){
            this.isCompleted = false;
            return true;
        }
        else{
            System.err.println("ERROR: Item count is negative.");
            return false;
        }
    }

}
