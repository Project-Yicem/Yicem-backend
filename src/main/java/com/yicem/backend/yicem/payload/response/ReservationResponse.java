package com.yicem.backend.yicem.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {
    private String id;
    private String sellerName;
    private String buyerName;
    private String offerName;
    private float offerPrice;
    private String timeslot;

    public ReservationResponse(String id, String sellerName, String offerName, float offerPrice, String timeslot) {
        this.id = id;
        this.sellerName = sellerName;
        this.offerName = offerName;
        this.offerPrice = offerPrice;
        this.timeslot = timeslot;
    }
}
