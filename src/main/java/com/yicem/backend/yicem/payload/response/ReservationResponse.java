package com.yicem.backend.yicem.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservationResponse {
    private String id;
    private String sellerName;
    private String offerName;
    private float offerPrice;
    private String timeslot;
}
